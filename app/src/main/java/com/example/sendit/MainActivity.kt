package com.example.sendit

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.sendit.ui.theme.SendItBackground
import com.example.sendit.ui.theme.SendItOrange
import com.example.sendit.ui.theme.SendItTheme
import com.example.sendit.ui.theme.SendItWhite
import androidx.activity.compose.setContent

// 1. Correction majeure : On hérite de FragmentActivity pour supporter les Fragments
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // On affiche l'UI Jetpack Compose directement
        setContent {
            SendItTheme {
                SendItApp()
            }
        }
    }
}

// 2. Fonction de navigation corrigée pour utiliser de vraies instances de Fragments
fun navigateToDestination(context: Context, destination: AppDestinations) {
    if (destination == AppDestinations.PUBLISH) return

    val targetFragment: Fragment = when (destination) {
        AppDestinations.MAP -> MapFragment()
        AppDestinations.GYM -> GymFragment()
        AppDestinations.NOTES -> NotesFragment()
        AppDestinations.PROFILE -> ProfileFragment()
        AppDestinations.PUBLISH -> return
    }

    if (context is FragmentActivity) {
        val fragmentManager = context.supportFragmentManager

        // On utilise l'ID généré dynamiquement par le conteneur Compose (voir plus bas)
        val containerId = View.generateViewId()

        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_id, targetFragment)
            .commit()
    }
}

@Composable
fun SendItBottomBar(
    currentDestination: AppDestinations,
    onDestinationChange: (AppDestinations) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navItemColors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent,
        selectedIconColor = SendItOrange,
        selectedTextColor = SendItOrange,
        unselectedIconColor = Color.Black,
        unselectedTextColor = Color.Black,
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        AppDestinations.entries.forEach { destination ->
            NavigationBarItem(
                icon = {
                    if (destination == AppDestinations.PUBLISH) {
                        Box(
                            modifier = Modifier
                                .size(width = 64.dp, height = 32.dp)
                                .background(
                                    color = SendItOrange,
                                    shape = RoundedCornerShape(8.dp),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painterResource(destination.icon),
                                contentDescription = destination.label,
                                tint = SendItWhite,
                            )
                        }
                    } else {
                        Icon(
                            painterResource(destination.icon),
                            contentDescription = destination.label,
                        )
                    }
                },
                label = { Text(destination.label) },
                selected = destination == currentDestination,
                onClick = { onDestinationChange(destination) },
                colors = navItemColors,
            )
        }
    }
}

@PreviewScreenSizes
@Composable
fun SendItApp(initialDestination: AppDestinations = AppDestinations.MAP) {
    var currentDestination by rememberSaveable { mutableStateOf(initialDestination) }
    val context = LocalContext.current

    SendItScaffold(
        currentDestination = currentDestination,
        onDestinationChange = { currentDestination = it },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // 3. Intégration magique : On crée un FrameLayout XML à la volée dans Compose
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    FrameLayout(ctx).apply {
                        id = R.id.fragment_container_id // On lui donne l'ID attendu par navigateToDestination
                    }
                },
                update = {
                    // Force l'affichage du premier fragment au démarrage
                    navigateToDestination(context, currentDestination)
                }
            )
        }
    }
}

@Composable
fun SendItScaffold(
    currentDestination: AppDestinations,
    onDestinationChange: (AppDestinations) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    var showPublishModal by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            SendItBottomBar(
                currentDestination = currentDestination,
                onDestinationChange = { destination ->
                    if (destination == AppDestinations.PUBLISH) {
                        showPublishModal = true
                    } else {
                        onDestinationChange(destination)
                    }
                },
            )
        },
    ) { innerPadding ->
        content(innerPadding)
    }

    if (showPublishModal) {
        PublishModal(
            onDismissRequest = { showPublishModal = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishModal(
    onDismissRequest: () -> Unit,
    onPublish: () -> Unit = onDismissRequest,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SendItBackground,
    ) {
        // Contenu de votre Bottom Sheet
    }
}

enum class AppDestinations(val label: String, val icon: Int) {
    MAP("Carte", R.drawable.ic_map),       // Remplacez ic_map par le nom de votre icône dans res/drawable
    GYM("Salles", R.drawable.ic_gym),     // Remplacez ic_gym par le nom de votre icône
    PUBLISH("Publier", R.drawable.ic_publish), // L'icône du milieu orange (ex: un bouton '+')
    NOTES("Notes", R.drawable.ic_notes),   // Remplacez ic_notes par le nom de votre icône
    PROFILE("Profil", R.drawable.ic_account) // Remplacez ic_profile par le nom de votre icône
}