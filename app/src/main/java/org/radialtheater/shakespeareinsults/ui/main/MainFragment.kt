package org.radialtheater.shakespeareinsults.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import org.radialtheater.shakespeareinsults.LOG_TAG
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.databinding.FragmentMainBinding
import org.radialtheater.shakespeareinsults.displayToast
import org.radialtheater.shakespeareinsults.shared.SharedViewModel
import org.radialtheater.shakespeareinsults.utilities.PreferenceHelper
import org.radialtheater.shakespeareinsults.utilities.ShareHelper

class MainFragment : Fragment(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: SharedViewModel
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private var generatingInsult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity())
            .get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create and configure binding, using fragment_main.xml layout
        val binding = FragmentMainBinding.inflate(
            inflater, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val toolbar = binding.root.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)

        initObservers()
        initNavDrawer(binding, toolbar)

        val gradient = binding.root.findViewById<View>(R.id.gradient_view)
        gradient.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val height = gradient.measuredHeight
                if (height > 0) {
                    gradient.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    //save height here and do whatever you want with it
                    Log.i(LOG_TAG, "Height = $height")
                    PreferenceHelper(requireContext()).setGradientHeight(height)
                }
            }
        })

        return binding.root
    }

    private fun initNavDrawer(
        binding: FragmentMainBinding,
        toolbar: Toolbar?
    ) {
        // Set up toggle button for nav drawer
        drawerLayout = binding.root.findViewById(R.id.drawer_layout)
        val navView = binding.root.findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            requireActivity(), drawerLayout, toolbar,
            R.string.open_nav_drawer, R.string.close_nav_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initObservers() {
        // Manage the button
        viewModel.buttonEnabled.observe(this, Observer {
            insultButton.isEnabled = it
            if (it.not()) insultButton.text = context?.getString(R.string.thinking_about_it)
        })

        // Track viewModel state
        viewModel.generatingInsult.observe(this, Observer {
            generatingInsult = it
        })

        // When insult generation is complete, navigate to result fragment
        viewModel.insult.observe(this, Observer {
            if (generatingInsult) {
                navController.navigate(R.id.action_nav_insult)
                generatingInsult = false
            }
        })
    }

    // Reset the UI
    override fun onResume() {
        super.onResume()
        requireActivity().title = getString(R.string.title_main_fragment)
        with(insultButton) {
            isEnabled = true
            text = context?.getString(R.string.insult_me)
        }
    }

    // Stop any sounds as the fragment is destroyed
    override fun onDestroy() {
        viewModel.stopGeneratingInsult()
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.action_last10 -> navigateTo(R.id.action_nav_last10)
            R.id.action_share_app -> ShareHelper().shareApp(requireActivity())
            R.id.action_settings -> navigateTo(R.id.action_nav_settings)
            R.id.action_display_about -> navigateTo(R.id.action_nav_about)
            else -> false
        }
    }

    /**
     * If not currently generating an insult, navigate somewhere
     */
    private fun navigateTo(destinationId: Int): Boolean {
        if (viewModel.generatingInsult.value!!.not()) {
            navController.navigate(destinationId)
        } else {
            displayToast(requireContext(), R.string.insulting_you)
        }
        return true
    }

}
