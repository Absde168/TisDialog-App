package ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ui.screens.auth.AuthScreen
import ui.screens.auth.register.RegisterScreen
import ui.screens.auth.balance.OtherBanksScreen
import ui.screens.auth.balance.TopUpBalanceScreen
import ui.screens.auth.chat.ChatSupportScreen
import ui.screens.auth.history.PaymentHistoryScreen
import ui.screens.auth.main.MainScreen
import ui.screens.auth.profile.ContactsScreen
import ui.screens.auth.profile.DocumentsScreen
import ui.screens.auth.profile.HelpFAQScreen
import ui.screens.auth.profile.NotificationSettingsScreen
import ui.screens.auth.profile.ProfileScreen
import ui.screens.auth.services.ServicesScreen
import ui.screens.auth.splash.SplashScreen
import ui.screens.auth.tariffs.TariffDetailScreen
import ui.screens.auth.tariffs.TariffsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("auth") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onAlreadyLoggedIn = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("auth") {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("main") {
            MainScreen(
                onProfileClick  = { navController.navigate("profile") },
                onChatClick     = { navController.navigate("chat") },
                onHistoryClick  = { navController.navigate("history") },
                onTariffsClick  = { navController.navigate("tariffs") },
                onServicesClick = { navController.navigate("services") },
                onTopUpClick    = { navController.navigate("topup") },
                onSessionExpired = {
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBackClick          = { navController.popBackStack() },
                onContactsClick      = { navController.navigate("contacts") },
                onSupportClick       = { navController.navigate("chat") },
                onDocumentsClick     = { navController.navigate("documents") },
                onNotificationsClick = { navController.navigate("notifications") },
                onHelpClick          = { navController.navigate("help") },
                onLogoutClick        = {
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onChatClick     = { navController.navigate("chat") },
                onTariffsClick  = { navController.navigate("tariffs") },
                onServicesClick = { navController.navigate("services") }
            )
        }

        composable("contacts") {
            ContactsScreen(onBackClick = { navController.popBackStack() })
        }

        composable("documents") {
            DocumentsScreen(onBackClick = { navController.popBackStack() })
        }

        composable("help") {
            HelpFAQScreen(
                onBackClick    = { navController.popBackStack() },
                onSupportClick = { navController.navigate("chat") }
            )
        }

        composable("notifications") {
            NotificationSettingsScreen(onBackClick = { navController.popBackStack() })
        }

        composable("tariffs") {
            TariffsScreen(
                onBackClick = { navController.popBackStack() },
                onTariffClick = { tariffName ->
                    navController.navigate("tariff_detail/$tariffName")
                },
                onMainClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = false }
                    }
                },
                onServicesClick = { navController.navigate("services") },
                onProfileClick = { navController.navigate("profile") },
                onChatClick = { navController.navigate("chat") },
                onPayClick = { navController.navigate("topup") }
            )
        }

        composable("tariff_detail/{tariffName}") { backStackEntry ->
            val tariffName = backStackEntry.arguments?.getString("tariffName") ?: ""
            TariffDetailScreen(
                tariffName = tariffName,
                onBackClick = { navController.popBackStack() },
                onConnectClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = false }
                    }
                }
            )
        }

        composable("services") {
            ServicesScreen(
                onBackClick = { navController.popBackStack() },
                onMainClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = false }
                    }
                },
                onTariffsClick = { navController.navigate("tariffs") },
                onProfileClick = { navController.navigate("profile") },
                onChatClick = { navController.navigate("chat") }
            )
        }

        composable("chat") {
            ChatSupportScreen(
                onProfileClick = { navController.navigate("profile") },
                onMainClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = false }
                    }
                },
                onTariffsClick = { navController.navigate("tariffs") },
                onServicesClick = { navController.navigate("services") }
            )
        }

        composable("history") {
            PaymentHistoryScreen(onBackClick = { navController.popBackStack() })
        }

        composable("topup") {
            TopUpBalanceScreen(
                onBackClick = { navController.popBackStack() },
                onOtherBanksClick = { navController.navigate("other_banks") }
            )
        }

        composable("other_banks") {
            OtherBanksScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
