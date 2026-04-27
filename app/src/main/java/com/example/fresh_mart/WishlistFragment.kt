package com.example.fresh_mart

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.Database.FreshMart
import com.example.fresh_mart.adapter.WishlistAdapter
import kotlinx.coroutines.*

class WishlistFragment : Fragment() {

    private lateinit var recyclerWishlist: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerWishlist = view.findViewById(R.id.recyclerWishlist)
        recyclerWishlist.layoutManager = LinearLayoutManager(requireContext())

        loadWishlist()
    }

    // 🔄 Load data from Room DB
    private fun loadWishlist() {
        val dao = FreshMart.getDatabase(requireContext()).wishlistDao()

        CoroutineScope(Dispatchers.IO).launch {
            val list = dao.getAllWishlist()

            withContext(Dispatchers.Main) {
                recyclerWishlist.adapter =
                    WishlistAdapter(list.toMutableList(), requireContext())
            }
        }
    }

    // 🔄 Refresh when coming back to fragment
    override fun onResume() {
        super.onResume()
        loadWishlist()
    }
}