package com.example.study.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.study.MyApp
import com.example.study.R
import com.example.study.viewmodel.PayViewModel
import com.sankuai.waimai.router.annotation.RouterUri

/**
 * Created by chenyy on 2022/4/18.
 */
@RouterUri(path = ["/pay"])
class PayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .background(Color(0xFFEEEEEE))
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PayTitle()
                OrderDetail()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    PayTypeLayout()
                }
                PayButton()
            }
        }
    }

    @Composable
    private fun PayButton() {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp, 14.dp, 12.dp, 30.dp)
        ) {
            val payText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontSize = 17.sp, fontWeight = FontWeight.W500)
                ) {
                    append("银行卡支付 ")
                }
                withStyle(
                    style = SpanStyle(fontSize = 12.sp, fontWeight = FontWeight.W500)
                ) {
                    append("¥")
                }
                withStyle(
                    style = SpanStyle(fontSize = 17.sp, fontWeight = FontWeight.W500)
                ) {
                    append("720.00")
                }
            }
            Text(
                modifier = Modifier
                    .clickable {
                        Toast
                            .makeText(MyApp.instance, "支付", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(6.dp))
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xffFFA50A),
                                Color(0xffFF7700)
                            )
                        )
                    )
                    .padding(vertical = 12.dp),
                text = payText,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }

    @Preview
    @Composable
    fun PayTypeLayout(payViewModel: PayViewModel = viewModel()) {
        Spacer(modifier = Modifier.height(32.dp))
        payViewModel.getSelfPayTypeModels()

        LazyColumn(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .background(
                    color = Color.White
                )
                .border(
                    8.dp, /*brush = Brush.verticalGradient(
                        listOf(
                            Color(0xffF5FAFF),
                            Color(0xffE7F3FE)
                        )
                    ),*/ shape = RoundedCornerShape(16.dp), color = Color(0xffF5FAFF)
                )
                .clip(RoundedCornerShape(16.dp))
        ) {
            item {
                Row(
                    modifier = Modifier
                        .height(54.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(11.dp))
                    AsyncImage(
                        modifier = Modifier
                            .width(25.dp)
                            .height(20.dp),
                        model = "https://raw.githubusercontent.com/chenyy0708/Images/master/img/20220418175233.png",
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "程支付",
                        style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.W500),
                        color = Color.Black,
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .background(Color(0xff287CF9), RoundedCornerShape(3.dp))
                            .padding(2.dp, 3.dp, 2.dp, 3.dp),
                    ) {
                        Text(
                            text = "携程官方",
                            style = TextStyle(fontSize = 13.sp),
                            color = Color.White
                        )
                    }

                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color(0xffE9EAEC),
                )
            }

            itemsIndexed(payViewModel.selfPayTypes) { index, item ->
                BankCardItem(item, payViewModel.selectedPayType.value) {
                    payViewModel.selectPayType(it)
                }
            }

            item {
                ChangePayType()
                TripPoint()
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color(0xffF5FAFF),
                )
            }

            itemsIndexed(payViewModel.thirdPayTypes) { index, item ->
                ThirdPayItem(item, payViewModel.selectedPayType.value) {
                    payViewModel.selectPayType(item)
                }
            }
        }
    }

    @Composable
    fun ThirdPayItem(
        item: ThirdPayModel, selectedPayType: PayTypeModel,
        onSelectPayType: (payTypeModel: PayTypeModel) -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectPayType(item) }
                .padding(horizontal = 11.dp, vertical = 23.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(thirdPayIcon(item.thirdType)),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 11.dp)
                        .size(23.dp)
                )
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = Color.Black,
                )
            }

            Image(
                painter = painterResource(id = if (selectedPayType is ThirdPayModel && selectedPayType.thirdType == item.thirdType) R.drawable.pay_type_item_seleted else R.drawable.pay_type_item_normal),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(19.dp, 19.dp)
            )
        }
    }

    private fun thirdPayIcon(payType: String): Int {
        return when (payType) {
            "aliPay" -> R.drawable.pay_icon_alipay_64_svg
            "weChat" -> R.drawable.pay_icon_ico_wechat
            "quickPay" -> R.drawable.pay_icon_quick_pass
            else -> R.drawable.pay_icon_alipay_64_svg
        }
    }

    @Composable
    private fun TripPoint() {
        val constraintSet = ConstraintSet {
            val pointIcon = createRefFor("pointIcon")
            val pointTitle = createRefFor("pointTitle")
            val pointMark = createRefFor("pointMark")
            val pointFraction = createRefFor("pointFraction")
            val checkIcon = createRefFor("checkIcon")
            constrain(pointIcon) {
                centerVerticallyTo(parent)
                start.linkTo(parent.start, margin = 16.dp)
            }
            constrain(pointTitle) {
                centerVerticallyTo(parent)
                start.linkTo(pointIcon.end, margin = 6.dp)
            }
            constrain(pointMark) {
                start.linkTo(pointTitle.end, margin = 4.dp)
                centerVerticallyTo(parent)
            }
            constrain(pointFraction) {
                centerVerticallyTo(parent)
                end.linkTo(checkIcon.start, margin = 6.dp)
            }
            constrain(checkIcon) {
                centerVerticallyTo(parent)
                end.linkTo(parent.end, margin = 10.dp)
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xffFFEDCF),
                            Color(0xffFFF6E8),
                            Color(0xffFFE9C4)
                        )
                    )
                )
                .padding(vertical = 18.dp),
            constraintSet = constraintSet
        ) {

            Image(
                painter = painterResource(R.drawable.pay_ctrip_integral_icon),
                contentDescription = null,
                modifier = Modifier
                    .layoutId("pointIcon")
                    .size(20.dp)
            )

            Text(
                text = "携程积分 (可抵 ¥10.00)",
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = Color.Black,
                modifier = Modifier.layoutId("pointTitle"),
            )

            Image(
                painter = painterResource(R.drawable.pay_discount_icon_info),
                contentDescription = null,
                modifier = Modifier
                    .layoutId("pointMark")
                    .size(12.dp)
            )

            Text(
                text = "消耗 2500分",
                fontSize = 11.sp,
                fontWeight = FontWeight.W400,
                color = Color(0xff666666),
                modifier = Modifier.layoutId("pointFraction"),
            )

            Image(
                painter = painterResource(id = R.drawable.pay_type_item_normal),
                contentDescription = null,
                modifier = Modifier
                    .layoutId("checkIcon")
                    .size(19.dp, 19.dp)
            )
        }
    }

    @Composable
    private fun ChangePayType() {
        Row(
            modifier = Modifier.padding(horizontal = 45.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "换卡支付，查看更多活动",
                fontSize = 12.sp,
                color = Color(0xff666666),
            )
            Box(
                modifier = Modifier
                    .padding(start = 6.dp, end = 6.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xffFF1317))
            )
            Image(
                painter = painterResource(R.drawable.pay_arraw_right),
                contentDescription = null,
                modifier = Modifier
                    .size(10.dp, 10.dp)
            )
        }
    }

    @Composable
    fun BankCardItem(
        item: SelfPayModel,
        selectedPayType: PayTypeModel,
        isHome: Boolean = true,
        onSelectPayType: (payTypeModel: PayTypeModel) -> Unit
    ) {
        val constraintSet = ConstraintSet {
            val bankIcon = createRefFor("bankIcon")
            val bankName = createRefFor("bankName")
            val discount = createRefFor("discount")
            val checkIcon = createRefFor("checkIcon")
            val divider = createRefFor("divider")
            constrain(bankIcon) {
                centerVerticallyTo(parent)
                start.linkTo(parent.start, margin = 8.dp)
            }
            constrain(bankName) {
                top.linkTo(parent.top, margin = 14.dp)
                start.linkTo(parent.start, margin = 44.dp)
            }
            constrain(discount) {
                width = Dimension.wrapContent
                top.linkTo(bankName.bottom, margin = 8.dp)
                bottom.linkTo(parent.bottom, margin = 14.dp)
                start.linkTo(bankName.start)
            }
            constrain(checkIcon) {
                centerVerticallyTo(parent)
                end.linkTo(parent.end, margin = 14.dp)
            }
            constrain(divider) {
                bottom.linkTo(parent.bottom)
                start.linkTo(bankName.start)
                width = Dimension.fillToConstraints
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectPayType(item) },
            constraintSet = constraintSet
        ) {
            if (!isHome) {
                AsyncImage(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .clip(shape = CircleShape)
                        .layoutId("bankIcon"),
                    model = "https://img2.baidu.com/it/u=2147843660,3054818539&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313",
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            Text(
                text = item.title,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.layoutId("bankName"),
            )
            Box(
                modifier = Modifier
                    .background(Color(0xffFEF1E5), RoundedCornerShape(2.dp))
                    .padding(4.dp, 2.dp, 2.dp, 4.dp)
                    .layoutId("discount")
            ) {
                Text(
                    text = item.discount,
                    fontSize = 11.sp,
                    color = Color(0xffFF7701),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                painter = painterResource(id = if (selectedPayType is SelfPayModel && selectedPayType.cardInfoId == item.cardInfoId) R.drawable.pay_type_item_seleted else R.drawable.pay_type_item_normal),
                contentDescription = null,
                modifier = Modifier
                    .layoutId("checkIcon")
                    .size(19.dp, 19.dp)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .layoutId("divider"),
                color = Color(0xffE9EAEC),
            )
        }
    }

    data class SelfPayModel(
        var title: String = "",
        var discount: String = "",
        var cardInfoId: String = ""
    ) : PayTypeModel("bankCard")

    data class ThirdPayModel(
        var title: String = "",
        var discount: String = "",
        var thirdType: String = ""
    ) : PayTypeModel("thirdPay")

    open class PayTypeModel(var payType: String)

    @Composable
    @Preview
    private fun OrderDetail() {
        Spacer(modifier = Modifier.height(15.dp))
        val oderAmount = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 24.sp)) {
                append("¥")
            }
            withStyle(style = SpanStyle(fontSize = 34.sp)) {
                append("720.00")
            }
        }
        Text(text = oderAmount, color = Color(0xff333333))
        Spacer(modifier = Modifier.height(15.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "上海世博中心亚朵 3月26号",
                style = TextStyle(fontSize = 12.sp),
                color = Color(0xff666666),
            )
            Image(
                painter = painterResource(R.drawable.pay_discount_more),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .width(10.dp)
                    .height(10.dp)
            )
        }
    }

    @Composable
    @Preview
    private fun PayTitle() {
        Box(
            modifier = Modifier
                .height(44.dp)
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Icon(
                Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = "安全支付",
                style = TextStyle(fontSize = 17.sp),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(
                    Alignment.Center
                ),
                color = Color(0xff333333)
            )
        }
    }
}