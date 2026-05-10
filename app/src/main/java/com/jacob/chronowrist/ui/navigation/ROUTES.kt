package com.jacob.chronowrist.ui.navigation
/*
enum class ROUTES {
    Login,
    Register,
    ForgotPassword,
    Home
}*/
// ── Route Definitions ──────────────────────────────────────────────────────

enum class ROUTES {
    Login,
    Register,
    ForgotPassword,
    Home
}

// ── Nav Graph ──────────────────────────────────────────────────────────────

// Add this to your existing NavHost in AppNavGraph.kt (or equivalent):
//
// @Composable
// fun AppNavGraph(
//     navController: NavHostController = rememberNavController()
// ) {
//     // Shared CartViewModel scoped to the NavGraph so it persists across screens
//     val cartViewModel: CartViewModel = viewModel()
//
//     NavHost(
//         navController = navController,
//         startDestination = ROUTES.Login.name
//     ) {
//         composable(ROUTES.Login.name) {
//             LoginScreen(navController = navController)
//         }
//         composable(ROUTES.Register.name) {
//             RegisterScreen(navController = navController)
//         }
//         composable(ROUTES.ForgotPassword.name) {
//             ForgotPasswordScreen(navController = navController)
//         }
//         composable(ROUTES.Home.name) {
//             HomeScreen(
//                 navController = navController,
//                 cartViewModel = cartViewModel   // pass the shared instance
//             )
//         }
//     }
// }
//
// In LoginViewModel.login() / RegisterViewModel.register(), navigate on success:
//     navController.navigate(ROUTES.Home.name) {
//         popUpTo(ROUTES.Login.name) { inclusive = true }
//     }

