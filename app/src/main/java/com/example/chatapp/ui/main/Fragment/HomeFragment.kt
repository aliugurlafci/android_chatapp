package com.example.chatapp.ui.main.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.ui.main.Adaptor.HomeAdapter
import com.example.chatapp.ui.main.Channel
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.Service.BackgroundService
import com.example.chatapp.ui.main.User
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A placeholder fragment containing a simple view.
 */
class HomeFragment : Fragment() {

    private lateinit var list: MutableList<Channel?>
    private lateinit var pageViewModel: PageViewModel
    private lateinit var root: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list = mutableListOf()
        //BackgroundService().startService(Intent(root.context,BackgroundService::class.java))
        root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.homeRecyclerView)
        val loadingComp = root.findViewById<ProgressBar>(R.id.loadingState)
        val current=PageViewModel.getUserUI().value?.u_name

        list = pageViewModel.getChannel(current)
        Handler().postDelayed({
            homeRecyclerView.adapter?.notifyDataSetChanged()
            recyclerView.adapter = HomeAdapter(list, root.context)
            recyclerView.layoutManager =
                LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            recyclerView.visibility = View.VISIBLE
            loadingComp.visibility = View.GONE
        }, 1000)

        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
