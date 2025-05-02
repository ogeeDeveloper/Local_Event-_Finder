package com.ogeedeveloper.local_event_finder_frontend.ui.screens.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Location
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
    private val savedStateHandle: SavedStateHandle
    // private val eventRepository: EventRepository, // Would be injected in a real app
    // private val bookingRepository: BookingRepository // Would be injected in a real app
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
            // In a real app, this would fetch from a repository
            // try {
            //     val event = eventRepository.getEventById(eventId)
            //     _uiState.update { it.copy(event = event, isLoading = false) }
            // } catch (e: Exception) {
            //     _uiState.update { it.copy(error = e.message ?: "Unknown error", isLoading = false) }
            // }
            
            // For now, we'll use dummy data
            _uiState.update { it.copy(event = getSampleEvent(eventId), isLoading = false) }
        }
    }
    
    fun updateQuantity(quantity: Int) {
        if (quantity > 0) {
            _uiState.update { it.copy(quantity = quantity) }
            calculateTotals()
        }
    }
    
    fun selectPaymentMethod(paymentMethod: String) {
        _uiState.update { it.copy(selectedPaymentMethod = paymentMethod) }
    }
    
    fun applyVoucher(voucherCode: String) {
        // In a real app, this would validate the voucher with an API call
        _uiState.update { 
            it.copy(
                voucherCode = voucherCode,
                voucherDiscount = 10.0 // Dummy discount amount
            ) 
        }
        calculateTotals()
    }
    
    fun processPayment(): String {
        // In a real app, this would make an API call to process the payment
        val transactionId = "TRX" + UUID.randomUUID().toString().substring(0, 8)
        
        _uiState.update { 
            it.copy(
                transactionId = transactionId,
                paymentStatus = PaymentStatus.COMPLETED
            ) 
        }
        
        return transactionId
    }
    
    private fun calculateTotals() {
        val event = _uiState.value.event ?: return
        val quantity = _uiState.value.quantity
        
        val subtotal = event.price * quantity
        val discount = if (quantity > 1) 20.0 else 0.0 // Example bulk discount
        val voucherDiscount = _uiState.value.voucherDiscount
        val taxAmount = if (event.price > 0) 0.10 else 0.0 // Tax only applies to paid events
        
        val total = subtotal - discount - voucherDiscount + taxAmount
        
        _uiState.update { 
            it.copy(
                subtotal = subtotal,
                discount = discount,
                taxAmount = taxAmount,
                totalAmount = total
            ) 
        }
    }
    
    // Sample data for development
    private fun getSampleEvent(eventId: String): Event {
        return Event(
            id = eventId,
            title = "Coldplay Ticket",
            description = "Coldplay are a British rock band formed in London in 1996. The band consists of vocalist, rhythm guitarist, and pianist Chris Martin, lead guitarist Jonny Buckland, bassist Guy Berryman, drummer Will Champion, and creative director Phil Harvey.",
            organizer = "Cold Play",
            organizerLogoUrl = "https://i.imgur.com/6Woi0Bf.jpg",
            startDate = Date(),
            endDate = Date(System.currentTimeMillis() + 3600000), // 1 hour later
            location = Location(
                id = "1",
                name = "Wembley Stadium",
                address = "London, UK",
                latitude = 51.556,
                longitude = -0.279
            ),
            imageUrl = "https://i.imgur.com/6Woi0Bf.jpg",
            price = 100.0,
            currency = "USD",
            category = "Entertainment"
        )
    }
}

/**
 * UI state for the booking flow
 */
data class BookingUiState(
    val event: Event? = null,
    val quantity: Int = 1,
    val selectedPaymentMethod: String = "Paypal",
    val voucherCode: String = "",
    val voucherDiscount: Double = 0.0,
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val taxAmount: Double = 0.0,
    val totalAmount: Double = 0.0,
    val transactionId: String = "",
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val isLoading: Boolean = true,
    val error: String? = null
)

enum class PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}
