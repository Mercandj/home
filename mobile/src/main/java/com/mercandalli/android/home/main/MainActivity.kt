package com.mercandalli.android.home.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.mercandalli.android.home.R
import com.mercandalli.android.home.train.TrainTrafficCardView
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core.train.TrainManager

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var trainManager: TrainManager
    private val trainSyncListener = createTrainSyncListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.activity_main_recycler_view)
        recyclerView.layoutManager = createLayoutManager()
        trainManager = CoreGraph.get().provideTrainManager()
        trainManager.registerTrainSyncListener(trainSyncListener)

        adapter = MainAdapter(createOnTrainTrafficClickListener())
        swipeRefreshLayout = findViewById(R.id.activity_main_swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            trainManager.sync()
        }
        recyclerView.adapter = adapter

        val traffics = ArrayList<MainAdapter.TrainTrafficViewModel>()
        traffics.add(MainAdapter.TrainTrafficViewModel("Rer D", TrainManager.TRAFFIC_D, null))
        traffics.add(MainAdapter.TrainTrafficViewModel("Metro 9", TrainManager.TRAFFIC_9, null))
        traffics.add(MainAdapter.TrainTrafficViewModel("Metro 14", TrainManager.TRAFFIC_14, null))
        traffics.add(MainAdapter.TrainTrafficViewModel("Rer A", TrainManager.TRAFFIC_A, null))
        val schedules = ArrayList<MainAdapter.TrainSchedulesViewModel>()
        schedules.add(MainAdapter.TrainSchedulesViewModel("Yerres D", TrainManager.SCHEDULES_YERRES_D, null))
        schedules.add(MainAdapter.TrainSchedulesViewModel("Gare de lyon D", TrainManager.SCHEDULES_GARE_DE_LYON_D, null))
        schedules.add(MainAdapter.TrainSchedulesViewModel("Gare de lyon A", TrainManager.SCHEDULES_GARE_DE_LYON_A, null))
        schedules.add(MainAdapter.TrainSchedulesViewModel("Boissy A", TrainManager.SCHEDULES_BOISSY_A, null))
        adapter.setViewModel(traffics, schedules)

        if (savedInstanceState == null) {
            trainManager.sync()
        }
    }

    override fun onDestroy() {
        trainManager.unregisterTrainSyncListener(trainSyncListener)
        super.onDestroy()
    }

    private fun createTrainSyncListener(): TrainManager.TrainSyncListener {
        return object : TrainManager.TrainSyncListener {
            override fun onTrainSyncFinished() {
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }

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
