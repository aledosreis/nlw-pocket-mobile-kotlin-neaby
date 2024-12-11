package com.alessandroreis.nlw.nearby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alessandroreis.nlw.nearby.data.model.Market
import com.alessandroreis.nlw.nearby.ui.screen.home.HomeScreen
import com.alessandroreis.nlw.nearby.ui.screen.home.HomeViewModel
import com.alessandroreis.nlw.nearby.ui.screen.market_details.MarketDetailsScreen
import com.alessandroreis.nlw.nearby.ui.screen.splash.SplashScreen
import com.alessandroreis.nlw.nearby.ui.screen.welcome.WelcomeScreen
import com.alessandroreis.nlw.nearby.ui.route.Home
import com.alessandroreis.nlw.nearby.ui.route.QRCodeScanner
import com.alessandroreis.nlw.nearby.ui.route.Splash
import com.alessandroreis.nlw.nearby.ui.route.Welcome
import com.alessandroreis.nlw.nearby.ui.screen.market_details.MarketDetailsUIEvent
import com.alessandroreis.nlw.nearby.ui.screen.market_details.MarketDetailsViewModel
import com.alessandroreis.nlw.nearby.ui.screen.qrcode_scanner.QRCodeScannerScreen
import com.alessandroreis.nlw.nearby.ui.theme.NearbyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NearbyTheme {
                val navController = rememberNavController()

                val homeViewModel by viewModels<HomeViewModel>()
                val homeUIState by homeViewModel.uiState.collectAsStateWithLifecycle()

                val marketDetailsViewModel by viewModels<MarketDetailsViewModel>()
                val marketDetailsUIState by marketDetailsViewModel.uiState.collectAsStateWithLifecycle()

                NavHost(
                    navController = navController,
                    startDestination = Splash
                ) {
                    composable<Splash> {
                        SplashScreen(
                            onNavigateToWelcome = {
                                navController.navigate(Welcome)
                            }
                        )
                    }
                    composable<Welcome> {
                        WelcomeScreen(
                            onNavigateToHome = {
                                navController.navigate(Home)
                            }
                        )
                    }
                    composable<Home> {
                        HomeScreen(
                            onNavigateToMarketDetails = { selectedMarket ->
                                navController.navigate(selectedMarket)
                            },
                            uiState = homeUIState,
                            onEvent = homeViewModel::onEvent
                        )
                    }
                    composable<Market> {
                        val selectedMarket = it.toRoute<Market>()

                        MarketDetailsScreen(
                            market = selectedMarket,
                            uiState = marketDetailsUIState,
                            onEvent = marketDetailsViewModel::onEvent,
                            onNavigateToQRCodeScanner = {
                                navController.navigate(QRCodeScanner)
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<QRCodeScanner> {
                        QRCodeScannerScreen(
                            onCompletedScan = { qrCodeContent ->
                                if (qrCodeContent.isNotEmpty())
                                    marketDetailsViewModel.onEvent(
                                        MarketDetailsUIEvent.OnFetchCoupon(
                                            qrCodeContent = qrCodeContent
                                        )
                                    )

                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}