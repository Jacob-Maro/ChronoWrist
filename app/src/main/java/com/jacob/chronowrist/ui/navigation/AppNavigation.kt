package com.jacob.chronowrist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jacob.chronowrist.ui.screens.SplashScreen
import com.jacob.chronowrist.ui.screens.authentication.forgotpassword.ForgotPasswordScreen
import com.jacob.chronowrist.ui.screens.authentication.login.LoginScreen
import com.jacob.chronowrist.ui.screens.authentication.register.RegisterScreen
import com.jacob.chronowrist.ui.screens.home.HomeScreen
import com.jacob.chronowrist.ui.screens.home.TrackOrderScreen
import com.jacob.chronowrist.ui.viewmodel.CartViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val cartViewModel: CartViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = ROUTES.Splash.name,
        modifier = modifier
    ) {
        composable(ROUTES.Splash.name) {
            SplashScreen(onTimeout = {
                navController.navigate(ROUTES.Login.name) {
                    popUpTo(ROUTES.Splash.name) { inclusive = true }
                }
            })
        }
        composable(ROUTES.Login.name) {
            LoginScreen(
                navController = navController,
                modifier = modifier
            )
        }
        composable(ROUTES.ForgotPassword.name) {
            ForgotPasswordScreen(
                navController = navController,
                modifier = modifier
            )
        }
        composable(ROUTES.Register.name) {
            RegisterScreen(
                navController = navController,
                modifier = modifier
            )
        }
        composable(ROUTES.Home.name) {
            HomeScreen(
                navController = navController,
                modifier = modifier,
                cartViewModel = cartViewModel
            )
        }
        composable("track_order/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            TrackOrderScreen(orderId = orderId, navController = navController)
        }
    }
}