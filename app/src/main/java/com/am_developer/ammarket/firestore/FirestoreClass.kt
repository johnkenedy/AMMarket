package com.am_developer.ammarket.firestore

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.am_developer.ammarket.models.*
import com.am_developer.ammarket.ui.activities.*
import com.am_developer.ammarket.ui.fragment.*
import com.am_developer.ammarket.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(fragment: RegisterFragment, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                fragment.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    @SuppressLint("CommitPrefEdits")
    fun getUserDetails(fragment: Fragment) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    fragment.context?.getSharedPreferences(
                        Constants.AM_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    user.name
                )
                editor.apply()

                when (fragment) {
                    is LoginFragment -> {
                        fragment.userLoggedInSuccess(user)
                    }
                    is ProfileFragment -> {
                        fragment.userDetailsSuccess(user)
                    }
                    is UserDetailsFragment -> {
                        fragment.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is LoginFragment -> {
                        fragment.hideProgressDialog()
                    }
                    is ProfileFragment -> {
                        fragment.hideProgressDialog()
                    }
                    is UserDetailsFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )

            }
    }

    fun updateUserProfileData(fragment: Fragment, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {

                when (fragment) {
                    is ProfileFragment -> {
                        fragment.userProfileUpdateSuccess()
                    }
                    is UserDetailsFragment -> {
                        fragment.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is ProfileFragment -> {
                        fragment.hideProgressDialog()
                    }
                    is UserDetailsFragment -> {
                        fragment.hideProgressDialog()
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while updating the user details.", e
                )

            }
    }

    fun uploadImageToCloudStorage(fragment: Fragment, imageFileURI: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "." +
                    Constants.getFileExtension(fragment, imageFileURI)
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when (fragment) {
                        is ProfileFragment -> {
                            fragment.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener { exception ->
                when (fragment) {
                    is ProfileFragment -> {
                        fragment.hideProgressDialog()
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }


    fun getProductsList(fragment: Fragment) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }

                when (fragment) {
                    is HomeFragment ->
                        fragment.successProductsListFromFireStore(productsList)
                    is CartFragment ->
                        fragment.successProductsListFromFireStore(productsList)
                }

            }
    }

    fun getProductsListToActivity(activity: Activity) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }

                when (activity) {
                    is ProductDetailsActivity ->
                        activity.successRelatedProductsListFromFireStore(productsList)

                }

            }
    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String) {
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsSuccess(product)
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }


    fun getProductFavDetails(fragment: Fragment, productId: String) {
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                when (fragment) {
                    is FavoriteFragment -> {
                        val product = document.toObject(Product::class.java)
                        if (product != null) {
                            fragment.getProductFavDetailsSuccess(product)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is FavoriteFragment -> {
                        fragment.hideProgressDialog()
                        Log.e(
                            fragment.javaClass.simpleName,
                            "Error while getting the product details.",
                            e
                        )
                    }
                }
            }
    }

    fun addCartItems(activity: ProductDetailsActivity, addToCart: CartItem) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for cart item.",
                    e
                )

            }
    }

    fun addFavToCartItems(fragment: Fragment, addToCart: CartItem) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                when (fragment) {
                    is FavoriteFragment -> {
                        fragment.addToCartSuccess()

                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is FavoriteFragment -> {
                        fragment.hideProgressDialog()

                        Log.e(
                            fragment.javaClass.simpleName,
                            "Error while creating the document for cart item.",
                            e
                        )
                    }
                }
            }
    }

    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {

                when (context) {
                    is CartListActivity -> {
                        context.itemUpdateSuccess()
                    }
                }

            }.addOnFailureListener { e ->
                when (context) {
                    is CartListActivity -> {
//                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName, "Error while updating the cart item.", e)
            }
    }

    fun checkIfItemExistInCart(activity: ProductDetailsActivity, productId: String) {
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->
                if (document.documents.size > 0) {
                    activity.productExistsInCart()
                } else {
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )
            }
    }

    fun checkFromFragmentIfItemExistInCart(fragment: Fragment, productId: String) {
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->
                when (fragment) {
                    is FavoriteFragment -> {
                        if (document.documents.size > 0) {
                            fragment.productExistsInCart()
                        } else {
                            fragment.hideProgressDialog()
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is FavoriteFragment -> {
                        fragment.hideProgressDialog()
                        Log.e(
                            fragment.javaClass.simpleName,
                            "Error while checking the existing cart list.",
                            e
                        )
                    }

                }
            }
    }

    fun getCartList(activity: Activity) {
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<CartItem> = ArrayList()

                for (i in document.documents) {
                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.id = i.id

                    list.add(cartItem)
                }

                when (activity) {
                    is CartListActivity -> {
                        activity.successCartItemList(list)
                    }
                    is CheckoutActivity -> {
                        activity.successCartItemsList(list)
                    }

                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting the cart list items.",
                    e
                )
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }
            }.addOnFailureListener { e ->
                when (context) {
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from cart list.",
                    e
                )
            }
    }

    fun getAllProductsList(activity: Activity) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }
                when (activity) {
                    is CartListActivity -> {
                        activity.successProductsListFromFireStore(productsList)
                    }
                    is CheckoutActivity -> {
                        activity.successProductsListFromFireStore(productsList)
                    }
                }


            }.addOnFailureListener { e ->
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                        Log.e(
                            activity.javaClass.simpleName,
                            "Error while getting all products items.",
                            e
                        )

                    }
                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                        Log.e(
                            activity.javaClass.simpleName,
                            "Error while getting all products items.",
                            e
                        )
                    }
                }
            }
    }

    fun getAddressesList(activity: AddressListActivity) {
        mFireStore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val addressList: ArrayList<Address> = ArrayList()
                for (i in document.documents) {
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    addressList.add(address)
                }
                activity.successAddressListFromFireStore(addressList)
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting the address list.", e)
            }
    }

    fun addAddress(activity: AddEditAddAddressActivity, addressInfo: Address) {
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.addUpdateAddressSuccess()

            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while adding the address.", e)
            }
    }

    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<CartItem>) {
        val writeBatch = mFireStore.batch()

        for (cartItem in cartList) {

            val productHasMap = HashMap<String, Any>()
            productHasMap[Constants.STOCK_QUANTITY] =
                (cartItem.stock_quantity.toInt() - cartItem.cart_quantity.toInt()).toString()

            val documentReference = mFireStore.collection(Constants.PRODUCTS)
                .document(cartItem.product_id)

            writeBatch.update(documentReference, productHasMap)
        }

        for (cartItem in cartList) {
            val documentReference = mFireStore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            activity.allDetailsUpdatedSuccessfully()
        }.addOnFailureListener { e ->
            activity.hideProgressDialog()
            Log.e(
                activity.javaClass.simpleName,
                "Error while updating all the details after order placed.",
                e
            )
        }

    }

    fun getMyOrdersList(fragment: MyPurchasesFragment) {
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Order> = ArrayList()
                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }

                fragment.populateOrdersListUnUI(list)

            }.addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }
    }

    fun placeOrder(activity: CheckoutActivity, order: Order) {
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {

                activity.orderPlacedSuccessfully()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while placing an order.", e
                )
            }
    }

    fun deleteAddress(activity: AddressListActivity, addressId: String) {
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                activity.deleteAddressSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while deleting the Address.", e)
            }
    }

    fun updateAddress(
        activity: AddEditAddAddressActivity,
        addressInfo: Address,
        addressId: String
    ) {
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while updating the Address.", e)
            }
    }

}