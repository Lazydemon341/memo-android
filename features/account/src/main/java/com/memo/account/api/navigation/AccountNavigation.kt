package com.memo.account.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.memo.account.internal.ui.login.LoginRoute
import com.memo.account.internal.ui.signup.SignUpRoute

const val loginNavigationRoute = "login_route"
const val signUpNavigationRoute = "sign_up_route"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    this.navigate(loginNavigationRoute, navOptions)
}

fun NavGraphBuilder.loginScreen(
    onBackPressed: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = loginNavigationRoute) {
        LoginRoute(
            onBackPressed = onBackPressed,
            onNavigateToSignUp = onNavigateToSignUp,
            onLoginSuccess = onLoginSuccess,
        )
    }
    nestedGraphs()
}

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    this.navigate(signUpNavigationRoute, navOptions)
}

fun NavGraphBuilder.signUpScreen(onBackPressed: () -> Unit) {
    composable(route = signUpNavigationRoute) {
        SignUpRoute(onBackPressed)
    }
}
