package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create.steps

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AppTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.components.DropdownInput
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

@Composable
fun EventDetailsStep(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    imageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Event Title
        Text(
            text = "Event Title",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = { Text("Enter event title") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event Description
        Text(
            text = "Event Description",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = { Text("Describe your event...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp),
            maxLines = 5
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event Category
        Text(
            text = "Event Category",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        CategoryDropdown(
            selectedCategory = category,
            onCategorySelected = onCategoryChange,
            categories = getSampleCategories()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event Image
        Text(
            text = "Event Image",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        ImageUploadBox(
            imageUri = imageUri,
            onImageClick = { imagePickerLauncher.launch("image/*") }
        )
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categories: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { },
            placeholder = { Text("Select category") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select category"
                )
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp)
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ImageUploadBox(
    imageUri: Uri?,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White)
            .clickable(onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            // Display the selected image using Coil
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = imageUri)
                        .build()
                ),
                contentDescription = "Selected image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_image_placeholder),
                    contentDescription = "Upload image",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(8.dp)
                )
                
                Text(
                    text = "Click to upload image",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Text(
                    text = "PNG, JPG or WEBP (max 5MB)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// Sample data
private fun getSampleCategories(): List<String> {
    return listOf(
        "Music",
        "Business",
        "Food & Drink",
        "Community",
        "Arts",
        "Film & Media",
        "Sports & Fitness",
        "Health",
        "Science & Tech",
        "Travel & Outdoor",
        "Charity & Causes",
        "Religion & Spirituality",
        "Family & Education",
        "Seasonal",
        "Government",
        "Fashion",
        "Home & Lifestyle",
        "Auto, Boat & Air",
        "Hobbies",
        "Other"
    )
}

@Preview(showBackground = true)
@Composable
fun EventDetailsStepPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            EventDetailsStep(
                title = "",
                onTitleChange = {},
                description = "",
                onDescriptionChange = {},
                category = "",
                onCategoryChange = {},
                imageUri = null,
                onImageSelected = {}
            )
        }
    }
}
