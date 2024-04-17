package com.blockyheadman.arcoscompanion.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.ExperimentalWearMaterial3Api
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.rememberPageIndicatorState
import com.blockyheadman.arcoscompanion.R
import com.blockyheadman.arcoscompanion.ui.theme.ArcOSCompanionTheme
import kotlinx.coroutines.launch

sealed class OnboardingPage(
    @DrawableRes
    val image: Int?,
    val title: String,
    val description: String
) {
    data object Welcome : OnboardingPage(
        image = R.drawable.companion_logo,
        title = "ArcOS Companion",
        description = "Your companion for your ArcOS\n" +
                "Let's get you setup for an awesome experience."
    )

    data object StayCaughtUp : OnboardingPage(
        image = null,
        title = "Every server in one spot",
        description = "Your companion allows you to connect to many servers at once" +
                " and manage them on the go."
    )
}

@OptIn(ExperimentalWearMaterial3Api::class)
@Composable
fun OnboardingMainScreen(navController: NavController) {
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage.Welcome,
        OnboardingPage.StayCaughtUp
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val pagerIndicatorState = rememberPageIndicatorState(pageCount = pages.size) {
        0f
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.weight(10f),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onboardingPage = pages[position])
        }
        HorizontalPageIndicator(
            pageIndicatorState = pagerIndicatorState,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    navController.popBackStack()
                }
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                if (pagerState.currentPage == pages.size - 1) "Alright, let's do this!" else "Okay"
            )
        }
    }
}

@Composable
fun PagerScreen(onboardingPage: OnboardingPage) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            onboardingPage.title,
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 42.sp
        )
        onboardingPage.image?.let {
            Image(
                painterResource(it),
                contentDescription = null,
                modifier = Modifier.size(186.dp)
            )
        }
        Text(
            onboardingPage.description,
            modifier = Modifier.padding(horizontal = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun OnboardingMainScreenPreview() {
    ArcOSCompanionTheme(darkTheme = true, dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            OnboardingMainScreen(rememberNavController())
        }
    }
}