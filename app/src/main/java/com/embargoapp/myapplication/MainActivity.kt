package com.embargoapp.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.embargoapp.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class MainActivity : ComponentActivity() {
    private val viewModel = ExampleViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Screen(viewModel)
                }
            }
        }
    }
}

@Composable
fun Screen(viewModel: ExampleViewModel) {

    val state = viewModel.container.stateFlow.collectAsState().value

    TextField(value = state.text, onValueChange = { it ->
        viewModel.updateText(it)
    })

}

data class ViewState(
    val text: String = ""
)

sealed class SideEffect

class ExampleViewModel : ContainerHost<ViewState, SideEffect>, ViewModel() {

    override val container = container<ViewState, SideEffect>(ViewState())

    fun updateText(text: String) = intent {

        reduce {
            state.copy(text = text)
        }
    }
}