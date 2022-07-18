package com.example.study.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.example.study.MyApp
import com.example.study.logD
import com.example.study.ui.compose.ParentLayout
import com.example.study.ui.compose.ParentLayoutV2
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sankuai.waimai.router.annotation.RouterUri
import kotlinx.coroutines.launch

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
//            ParentLayout(
//                modifier = Modifier
//                    .padding(10.dp)
//                    .background(Color.Cyan)
//                    .padding(20.dp)
//                    .width(400.dp)
//                    .height(400.dp)
//            ) {
//                Text(text = "哈哈哈")
//                Text(text = "啦啦啦啦1")
//                Text(text = "啦啦啦啦2")
//                Text(text = "啦啦啦啦3")
//                Text(text = "啦啦啦啦4")
//                Text(text = "啦啦啦啦5")
//                Text(text = "啦啦啦啦6")
//                Text(text = "啦啦啦啦7")
//                Text(text = "啦啦啦啦8")
//                Text(text = "啦啦啦啦9")
//                Text(text = "啦啦啦啦10")
//                Text(text = "啦啦啦啦11")
//
//            }
//            val modifier = Modifier
//                .size(100.dp)
//                .background(Color.Red)
//                .padding(10.dp)
//                .background(Color.Blue)
//
//            val name = modifier.foldOut("name:") { element, s ->
//                logD("Modifier:${element::class.java.simpleName}")
//                s + element::class.java.simpleName
//            }
//            logD("Modifier---:${name}")
//
//            ParentLayoutV2(
//                Modifier
//                    .size(100.dp)
//                    .background(Color.Red)
//                    .padding(10.dp)
//                    .background(Color.Blue)
//            ) {
//                Text(text = "啦啦啦啦10")
//                Text(text = "啦啦啦啦11")
//            }
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
//                    BasicsComponents()
                    testFiled("测试文案1")
                }
                item {
                    LayoutComponents()
                }
                items(toDoList) {
                    TodoRow(todo = it)
                }
                item {
                    FooterComponents(onAddItem, onRemoveItem)
                }
            },
        )
    }

    @Composable
    private fun testFiled(
        textStr:String = ""
    ) {
        var text by remember { mutableStateOf(textStr) }
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "我是一个占位提示文案") },
//            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Filled.Lock,
//                    contentDescription = null
//                )
            }
        )
    }

    @Composable
    private fun FooterComponents(
        onAddItem: (TodoItem) -> Unit,
        onRemoveItem: (TodoItem) -> Unit
    ) {
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

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun LayoutComponents() {
        Text(text = "布局组件", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .background(color = Color.Red),
            contentAlignment = Alignment.Center

        ) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .background(color = Color.Blue)
                    .align(Alignment.TopStart)
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .background(color = Color.Green)
                    .align(Alignment.TopEnd)
            )

            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .background(color = Color.Yellow)
                    .align(Alignment.BottomEnd)
            )
        }
        var tabState by remember { mutableStateOf(0) }
        val titleList = listOf("Tab1", "Tab2", "Tab3")
        TabRow(selectedTabIndex = tabState) {
            titleList.forEachIndexed { index, value ->
                Tab(
                    text = { Text(value) },
                    selected = tabState == index,
                    onClick = {
                        tabState = index
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "第${tabState}个Tab, ${titleList[tabState]}",
            style = TextStyle(fontSize = 20.sp)
        )
        val datas = listOf(
            Pair(
                "马自达",
                "https://img0.baidu.com/it/u=2047125004,2767992774&fm=253&fmt=auto&app=120&f=JPEG?w=864&h=416"
            ),
            Pair(
                "领克",
                "https://img2.baidu.com/it/u=2410541573,3557804772&fm=253&fmt=auto&app=138&f=JPEG?w=815&h=500"
            ),
            Pair(
                "本田",
                "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=1ece7b70e1c27d1eba263dc42bd7adaf/3812b31bb051f8194ce9ca9e9fb44aed2f73e713.jpg"
            ),
            Pair(
                "宝马",
                "https://img2.baidu.com/it/u=64361011,3076899940&fm=253&fmt=auto&app=120&f=JPEG?w=888&h=500"
            ),
            Pair(
                "玛莎拉蒂",
                "https://img2.baidu.com/it/u=3400031569,3778812739&fm=253&fmt=auto&app=120&f=JPEG?w=769&h=500"
            )
        )
        val tabPager = rememberPagerState(0)
        val coroutineScope = rememberCoroutineScope()
        ScrollableTabRow(
            selectedTabIndex = tabPager.currentPage,
            modifier = Modifier
                .fillMaxWidth(),
            edgePadding = 6.dp
        ) {
            datas.forEachIndexed { index, value ->
                Tab(
                    text = {
                        Text(
                            datas[index].first,
                            fontSize = if (tabPager.currentPage == index) 18.sp else 14.sp
                        )
                    },
                    selected = tabPager.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            tabPager.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
        HorizontalPager(count = datas.size, state = tabPager) { page ->
            AsyncImage(
                model = datas[page % datas.size].second,
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        val constraintSet = ConstraintSet {
            val avatar = createRefFor("avatar")
            val title = createRefFor("title")
            val desc = createRefFor("desc")
            val rightIcon = createRefFor("rightIcon")

            constrain(avatar) {
                centerVerticallyTo(parent)
                start.linkTo(parent.start, margin = 8.dp)
            }

            constrain(title) {
                top.linkTo(avatar.top)
                start.linkTo(avatar.end, margin = 10.dp)
            }

            constrain(desc) {
                width = Dimension.fillToConstraints
                bottom.linkTo(avatar.bottom)
                end.linkTo(rightIcon.start, margin = 8.dp)
                start.linkTo(title.start)
            }

            constrain(rightIcon) {
                centerVerticallyTo(parent)
                end.linkTo(parent.end, margin = 8.dp)
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Cyan),
            constraintSet = constraintSet
        ) {
//            Icon(
//                Icons.Default.Airplay,
//                contentDescription = null,
//                modifier = Modifier.layoutId("rightIcon")
//            )

            Text(
                text = "标题",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.layoutId("title"),
            )
            Text(
                text = "我的内容我的内容我的内容我的内容我的内容我的内容我的内容我的内容我的内容我的内容",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.layoutId("desc"),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            AsyncImage(
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .clip(shape = CircleShape)
                    .border(width = 1.dp, Color.Green, shape = RoundedCornerShape(100.dp))
                    .layoutId("avatar"),
                model = "https://img2.baidu.com/it/u=2147843660,3054818539&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313",
                contentScale = ContentScale.Crop,
                contentDescription = null
            )


        }
    }

    @Composable
    @Preview
    fun BasicsComponents() {
        Text(text = "基本组件", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(20.dp))
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
//            Icon(
//                Icons.Filled.Book,
//                contentDescription = null,
//                tint = if (checkState) Color.Yellow else Color.Gray
//            )
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
//                Icon(
//                    imageVector = Icons.Filled.Lock,
//                    contentDescription = null
//                )
            }
        )
        var showImage by remember { mutableStateOf(true) }
        AnimatedVisibility(visible = showImage) {
            AsyncImage(
                modifier = Modifier
                    .size(200.dp, 100.dp)
                    .border(width = 2.dp, Color.Green, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        Toast
                            .makeText(MyApp.instance, "点击图片", Toast.LENGTH_SHORT)
                            .show()
                    },
                contentScale = ContentScale.Crop,
                model = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1113%2F052420110515%2F200524110515-2-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652778301&t=c39ef66de8bd35442e77c2bbc5616e79",
                contentDescription = null
            )
        }
        Button(onClick = { showImage = showImage.not() }) {
            Text(text = if (showImage) "隐藏" else "显示")
        }
    }

    @Composable
    private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
        TodoScreen(
            toDoList = viewModel.todoItems,
            onAddItem = todoViewModel::addItem,
            onRemoveItem = todoViewModel::removeItem
        )
    }

    @Preview
    @Composable
    fun TodoRow(
        @PreviewParameter(TodoPreviewParameterProvider::class) todo: TodoItem?,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(todo?.title ?: "")
            var selected by remember { mutableStateOf(todo?.checked ?: false) }
            Checkbox(checked = selected, onCheckedChange = {
                selected = it
                todo?.checked = selected
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

    class TodoPreviewParameterProvider : PreviewParameterProvider<TodoItem> {
        override val values = sequenceOf(
            TodoItem("哈哈哈", true)
        )
    }
}