package org.radialtheater.shakespeareinsults.ui.last10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.shared.SharedViewModel
import org.radialtheater.shakespeareinsults.utilities.PreferenceHelper

/**
 * Displays lasts 10 insults
 */
class Last10Fragment : Fragment(),
    InsultsAdapter.InsultItemListener {

    private lateinit var viewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a reference to the shared view model
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize views and controllers
        val view = inflater.inflate(R.layout.fragment_last10, container, false)
        val wordCloud = view.findViewById<ImageView>(R.id.word_cloud)
        val gradientView = view.findViewById<View>(R.id.gradient_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)

        setHasOptionsMenu(true)

        // Set up toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        // Observe data set
        viewModel.last10Insults.observe(this, Observer {
            val adapter = InsultsAdapter(it)
            recyclerView.adapter = adapter
            adapter.setItemListener(this)
        })

        // Resize word cloud and gradient overlay to match calculated height from main fragment
        val height = PreferenceHelper(requireContext()).getGradientHeight()
        val params = wordCloud.layoutParams
        params.height = height
        wordCloud.layoutParams = params
        gradientView.layoutParams = params
        return view
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = getString(R.string.title_last_10_fragment)
    }

    // Handle options menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Return to previous navigation destination
    private fun navigateUp(): Boolean {

        navController.navigateUp()
        return true
    }

    override fun onInsultItemClick(insult: String) {
        viewModel.insult.value = insult
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        navController.navigate(R.id.action_nav_insult)
    }

}
