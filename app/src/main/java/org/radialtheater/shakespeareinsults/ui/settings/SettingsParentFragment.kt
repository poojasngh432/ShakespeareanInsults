package org.radialtheater.shakespeareinsults.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.radialtheater.shakespeareinsults.R

class SettingsParentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        //  Set up toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        requireActivity().title = getString(R.string.settings)
        setHasOptionsMenu(true)

        return view
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
