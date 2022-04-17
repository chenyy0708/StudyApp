package com.example.study.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.study.MyApp
import com.sankuai.waimai.router.annotation.RouterUri

/**
 * 描述:
 * 作者: ChenYy
 * 日期: 2022-04-10 16:56
 */
@RouterUri(path = ["/compose"])
class ComposeActivity : AppCompatActivity() {

    private val viewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.addItem((TodoItem("任务1")))
            TodoActivityScreen(todoViewModel = viewModel)
        }
    }

    @Composable
    fun TodoScreen(
        toDoList: List<TodoItem>,
        onAddItem: (TodoItem) -> Unit,
        onRemoveItem: (TodoItem) -> Unit
    ) {
        LazyColumn(
            content = {
                item {
                    Text(text = "基本组件", fontSize = 16.sp)
                    Text(
                        modifier = Modifier
                            .width(150.dp)
                            .alpha(0.6f)
                            .background(Color.LightGray),
                        text = "Jetpack Compose 是用于构建原生 Android 界面的新工具包。它可简化并加快 Android 上的界面开发，使用更少的代码、强大的工具和直观的 Kotlin API，快速让应用生动而精彩。",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    val builder = AnnotatedString.Builder().apply {
                        val style = SpanStyle(
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        pushStyle(
                            style
                        )
                        append("Jetpack Compose")
                        pop()
                        append(" 是用于构建原生 Android 界面的新工具包。它可简化并加快 Android 上的界面开发，使用更少的代码、强大的工具和直观的 ")
                        pushStyle(
                            style
                        )
                        append("Kotlin API")
                        pop()
                        append("，快速让应用生动而精彩。")
                    }
                    Text(
                        text = builder.toAnnotatedString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    Button(onClick = {
                        Toast.makeText(MyApp.instance, "按钮点击", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "普通的按钮，可点击")
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color.Yellow),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(text = "有样式的按钮")
                    }
                    var checkState by remember { mutableStateOf(false) }
                    IconToggleButton(checked = checkState, onCheckedChange = {
                        checkState = it
                    }) {
                        Icon(
                            Icons.Filled.Book,
                            contentDescription = null,
                            tint = if (checkState) Color.Yellow else Color.Gray
                        )
                    }
                    var text by remember { mutableStateOf("") }
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text(text = "我是一个占位提示文案") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null
                            )
                        }
                    )

                }
                items(toDoList) {
                    TodoRow(todo = it, onItemClicked = {
                        Toast.makeText(this@ComposeActivity, it.title, Toast.LENGTH_SHORT)
                            .show()
                    })
                }
                item {
                    Row {
                        Button(onClick = {
                            onAddItem(TodoItem("任务${viewModel.todoItems.size + 1}"))
                        }) {
                            Text(text = "添加任务")
                        }
                        Button(onClick = {
                            viewModel.todoItems.lastOrNull()?.let { onRemoveItem(it) }
                        }) {
                            Text(text = "移除任务")
                        }
                    }
                }
            },
        )
    }

    @Composable
    private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
        TodoScreen(
            toDoList = viewModel.todoItems,
            onAddItem = todoViewModel::addItem,
            onRemoveItem = todoViewModel::removeItem
        )
    }

    @Composable
    fun TodoRow(todo: TodoItem, onItemClicked: (TodoItem) -> Unit, modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onItemClicked(todo) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(todo.title)
            var selected by remember { mutableStateOf(todo.checked) }
            Checkbox(checked = selected, onCheckedChange = {
                selected = it
                todo.checked = selected
            })
        }
    }

    data class TodoItem(
        val title: String,
        var checked: Boolean = false
    )

    class TodoViewModel : ViewModel() {

        private var _todoItems = mutableStateListOf<TodoItem>()
        val todoItems: MutableList<TodoItem> = _todoItems

        fun addItem(item: TodoItem) {
            _todoItems.add(item)
        }

        fun removeItem(item: TodoItem) {
            _todoItems.remove(item)
        }
    }
}