package com.am_developer.ammarket.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.ActivityMyOrdersDetailsBinding
import com.am_developer.ammarket.models.Order
import com.am_developer.ammarket.ui.adapter.CartItemsListAdapter
import com.am_developer.ammarket.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class MyOrdersDetailsActivity : BaseActivity() {

    private lateinit var binding : ActivityMyOrdersDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyOrdersDetailsBinding.inflate(layoutInflater)

        var myOrderDetails: Order = Order()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)) {
            myOrderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }

        setupUI(myOrderDetails)

        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(orderDetails: Order) {
        binding.tvOrderDetailsId.text = orderDetails.title
        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime
        val orderDateTime = formatter.format(calendar.time)
        binding.tvOrderDetailsDate.text = orderDateTime
        binding.tvOrderStatus.text = orderDetails.shipping_progress

        binding.rvMyOrderItemsList.layoutManager = LinearLayoutManager(this@MyOrdersDetailsActivity)
        binding.rvMyOrderItemsList.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@MyOrdersDetailsActivity, orderDetails.items, false)
        binding.rvMyOrderItemsList.adapter = cartListAdapter

        binding.tvMyOrderDetailsAddressType.text = orderDetails.address.type
        binding.tvMyOrderDetailsFullName.text = orderDetails.address.name
        binding.tvMyOrderDetailsAddress.text = "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        binding.tvMyOrderDetailsAdditionalNote.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()) {
            binding.tvMyOrderDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvMyOrderDetailsOtherDetails.text = orderDetails.address.otherDetails
        } else {
            binding.tvMyOrderDetailsOtherDetails.visibility = View.GONE
        }

        binding.tvMyOrderDetailsMobileNumber.text = orderDetails.address.mobileNumber

        val subTotal = orderDetails.sub_total_amount
        val roundedSubTotal = "%.2f".format(subTotal).toDouble()
        binding.tvOrderDetailsSubTotal.text = roundedSubTotal.toString()
        binding.tvOrderDetailsShippingCharge.text = orderDetails.shipping_charge
        val total = orderDetails.total_amount
        val roundedTotal = "%.2f".format(total).toDouble()
        binding.tvOrderDetailsTotalAmount.text = roundedTotal.toString()

    }

}