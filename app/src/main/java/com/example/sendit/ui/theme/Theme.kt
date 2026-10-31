package com.example.sendit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color

private val SendItColorScheme = lightColorScheme(
    primary = SendItGreen,
    onPrimary = SendItWhite,
    primaryContainer = SendItSurfaceSoft,
    onPrimaryContainer = SendItGreen,

    secondary = SendItOrange,
    // Le vert foncé offre un meilleur contraste sur l’orange.
    onSecondary = SendItGreen,
    secondaryContainer = SendItOrangeLight,
    onSecondaryContainer = SendItGreen,

    tertiary = SendItHoldPurple,
    onTertiary = SendItGreen,

    background = SendItBackground,
    onBackground = SendItTextPrimary,

    surface = SendItWhite,
    onSurface = SendItTextPrimary,
    surfaceVariant = SendItSurfaceSoft,
    onSurfaceVariant = SendItTextSecondary,

    outline = SendItTextSecondary,
    outlineVariant = SendItBorder,

    inverseSurface = SendItGreen,
    inverseOnSurface = SendItBackground,
    inversePrimary = SendItSurfaceSoft,

    // Couleurs d’erreur ajoutées pour les formulaires.
    error = Color(0xFFBA1A1A),
    onError = SendItWhite,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    scrim = Color.Black
)

private val SendItTypography = Typography()

@Composable
fun SendItTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SendItColorScheme,
        typography = SendItTypography,
        content = content
    )
}
