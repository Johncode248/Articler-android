package com.lions.aplication.android.articler2

import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("register") { RegistrationContent(navController) }
        composable("login") { LoginContent(navController) }
        composable("home") { HomePage(navController)}
        composable("profile"){ ProfilePage(navController)}
        composable("addArticle"){ Workbench(navController)}

        // nav with args
        composable(
            route = "updateProfile/{name}/{password}",
            arguments = listOf(
                navArgument(name = "name"){
                    type = NavType.StringType
                },
                navArgument(name = "password"){
                    type = NavType.StringType
                }
            )

        ){ backstackEntry ->

            UpdateProfilePage(
                navController,
                myName = backstackEntry.arguments?.getString("name"),
                myPassword = backstackEntry.arguments?.getString("password")
            )
        }


        composable(
            route = "updateArticlePage/{title}/{summary}/{content}/{articleId}",
            arguments = listOf(
                navArgument(name = "title"){
                    type = NavType.StringType
                },
                navArgument(name = "summary"){
                    type = NavType.StringType
                },
                navArgument(name = "content"){
                    type = NavType.StringType
                },
                navArgument(name = "articleId"){
                    type = NavType.StringType
                }

            )
        ){backstackEntry ->
            UpdateArticlePage(
                navController,
                myTitle = backstackEntry.arguments?.getString("title"),
                mySummary = backstackEntry.arguments?.getString("summary"),
                myContent = backstackEntry.arguments?.getString("content"),
                myArticleId = backstackEntry.arguments?.getString("articleId")

            )
        }


        composable(
            route = "contentPage/{title}/{content}",
            arguments = listOf(
                navArgument(name = "title"){
                    type = NavType.StringType
                },
                navArgument(name = "content"){
                    type = NavType.StringType
                },
            )

        ){backstackEntry->
            ArticleContentPage(
                myTitle = backstackEntry.arguments?.getString("title"),
                myContent = backstackEntry.arguments?.getString("content")
                )
        }
    }
}