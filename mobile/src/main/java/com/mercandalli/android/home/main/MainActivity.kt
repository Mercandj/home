package com.mercandalli.android.home.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.mercandalli.android.home.R
import com.mercandalli.android.home.train.TrainTrafficCardView
import com.mercandalli.android.home.train.TrainTrafficViewModel
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core.train.TrainManager

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.activity_main_recycler_view)
        recyclerView.layoutManager = createLayoutManager()

        adapter = MainAdapter(createOnTrainTrafficClickListener())
        swipeRefreshLayout = findViewById(R.id.activity_main_swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
        }
        recyclerView.adapter = adapter

        val traffics = ArrayList<TrainTrafficViewModel>()
        traffics.add(TrainTrafficViewModel("Rer A", TrainManager.TRAFFIC_A, null))
        traffics.add(TrainTrafficViewModel("Rer D", TrainManager.TRAFFIC_D, null))
        traffics.add(TrainTrafficViewModel("Metro 9", TrainManager.TRAFFIC_9, null))
        traffics.add(TrainTrafficViewModel("Metro 14", TrainManager.TRAFFIC_14, null))
        adapter.setTrainTrafficViewModel(traffics)

        if (savedInstanceState == null) {
            CoreGraph.get().provideTrainManager().sync()
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager? {
        return StaggeredGridLayoutManager(
                resources.getInteger(R.integer.activity_main_nb_column),
                StaggeredGridLayoutManager.VERTICAL)
    }

    private fun createOnTrainTrafficClickListener(): TrainTrafficCardView.OnTrainTrafficClickListener {
        return object : TrainTrafficCardView.OnTrainTrafficClickListener {
            override fun onTrainTrafficClicked() {

            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
