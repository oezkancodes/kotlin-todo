package com.example.kotlintodo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.adapter.TodoAdapter
import com.example.kotlintodo.data.Datasource
import com.example.kotlintodo.databinding.ActivityTasksBinding
import com.google.firebase.auth.FirebaseAuth

class TasksActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarTasks.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_tasks)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        renderTodos()
        initClickHandlers()
    }

    /**
     * Fetch and render To-Dos.
     */
    private fun renderTodos() {
        Datasource().loadTodos { dataset ->
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = TodoAdapter(this, dataset)
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun initClickHandlers() {
        /**
         * Signout FAB
         */
        binding.appBarTasks.fabSignOut.setOnClickListener { view ->
            val user = FirebaseAuth.getInstance()
            user.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
        /**
         * Todo FAB
         */
        binding.appBarTasks.fabAddTodo.setOnClickListener { view ->
            startActivity(Intent(this, CreateTodoActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.tasks, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_tasks)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}