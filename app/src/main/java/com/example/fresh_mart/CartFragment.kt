package com.example.fresh_mart

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.Database.FreshMart
import com.example.fresh_mart.adapter.CartAdapter
import com.example.fresh_mart.dataclass.CartItem

class CartFragment : Fragment() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var txtEmpty: TextView

    private lateinit var adapter: CartAdapter
    private var cartList = mutableListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerCart = view.findViewById(R.id.cartRecycler)
        txtEmpty = view.findViewById(R.id.txtEmpty)

        recyclerCart.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Initialize adapter once
        adapter = CartAdapter(cartList, requireContext())
        recyclerCart.adapter = adapter

        // ✅ Observe Room DB (AUTO UPDATE)
        val dao = FreshMart.getDatabase(requireContext()).cartDao()

        dao.getAll().observe(viewLifecycleOwner) { list ->

            cartList.clear()
            cartList.addAll(list)

            if (cartList.isEmpty()) {
                txtEmpty.visibility = View.VISIBLE
                recyclerCart.visibility = View.GONE
            } else {
                txtEmpty.visibility = View.GONE
                recyclerCart.visibility = View.VISIBLE
            }

            adapter.notifyDataSetChanged()
        }
    }
}