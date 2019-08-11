package org.radialtheater.shakespeareinsults.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var viewModel: AboutViewModel

    // Using a ViewModel that's specific to this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAboutBinding.inflate(
            inflater, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //  Set up toolbar
        val toolbar = binding.root.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.about)

        return binding.root
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
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        navController.navigateUp()
        return true
    }

}
