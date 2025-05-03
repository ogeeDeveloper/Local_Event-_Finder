package com.ogeedeveloper.local_event_finder_frontend.ui.screens.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Location
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for the booking flow screens
 */
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        val eventId: String? = savedStateHandle.get<String>("eventId")
        if (!eventId.isNullOrEmpty()) {
            loadEvent(eventId)
        }
    }

    private fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                eventRepository.getEventDetails(eventId).collect { event ->
                    if (event != null) {
                        _uiState.update { it.copy(event = event, isLoading = false) }
                    } else {
                        _uiState.update { it.copy(error = "Event not found", isLoading = false) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error", isLoading = false) }
            }
        }
    }
    
    fun updateQuantity(newQuantity: Int) {
        if (newQuantity < 1) return // Don't allow less than 1 ticket
        
        viewModelScope.launch {
            _uiState.update { it.copy(quantity = newQuantity) }
            recalculateTotal()
        }
    }
    
    fun updatePaymentMethod(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }
    
    private fun recalculateTotal() {
        val event = _uiState.value.event ?: return
        val quantity = _uiState.value.quantity
        
        val ticketPrice = event.price
        val discount = if (quantity > 1) 20.0 else 0.0
        val taxAmount = if (event.price > 0) 0.10 else 0.0
        val subtotal = (ticketPrice * quantity)
        val total = subtotal - discount + taxAmount
        
        _uiState.update { 
            it.copy(
                subtotal = subtotal,
                discount = discount,
                taxAmount = taxAmount,
                totalAmount = total
            ) 
        }
    }
    
    // Apply a voucher code (simplified implementation)
    fun applyVoucher(code: String) {
        //TODO: In a real app, this would validate the voucher with an API call
        // For now, we'll just apply a fixed discount if any code is provided
        val additionalDiscount = if (code.isNotBlank()) 10.0 else 0.0
        
        // Recalculate totals with the voucher discount
        val event = _uiState.value.event ?: return
        val quantity = _uiState.value.quantity
        
        val ticketPrice = event.price
        val discount = if (quantity > 1) 20.0 else 0.0
        val taxAmount = if (event.price > 0) 0.10 else 0.0
        val subtotal = (ticketPrice * quantity)
        val total = subtotal - discount - additionalDiscount + taxAmount
        
        _uiState.update { 
            it.copy(
                subtotal = subtotal,
                discount = discount + additionalDiscount,
                taxAmount = taxAmount,
                totalAmount = total
            ) 
        }
    }
    
    // Process payment and generate transaction ID
    fun processPayment(): String {
        // TODO: In a real app, this would make an API call to process the payment
        val transactionId = "TRX" + UUID.randomUUID().toString().substring(0, 8)
        
        _uiState.update { 
            it.copy(
                transactionId = transactionId
                // Todo: Update payment status in the backend
            ) 
        }
        
        return transactionId
    }
}

/**
 * UI state for the booking screens
 */
data class BookingUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val event: Event? = null,
    val quantity: Int = 1,
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val taxAmount: Double = 0.0,
    val totalAmount: Double = 0.0,
    val selectedPaymentMethod: String? = null,
    val transactionId: String? = null
)

enum class PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED
}
