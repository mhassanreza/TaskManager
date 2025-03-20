package com.bigint.taskmanager.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.bigint.taskmanager.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val sansFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Merriweather Sans"),
        fontProvider = provider,
    )
)


// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = sansFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = sansFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = sansFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = sansFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = sansFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = sansFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = sansFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = sansFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = sansFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = sansFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = sansFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = sansFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = sansFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = sansFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = sansFontFamily)
)

