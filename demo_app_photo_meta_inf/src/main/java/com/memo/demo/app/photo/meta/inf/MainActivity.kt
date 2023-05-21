package com.memo.demo.app.photo.meta.inf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {

    private val reportWriter: ReportWriter = ReportWriterImpl()

    private val viewModel: MainScreenViewModel by viewModels {
        viewModelFactory {
            initializer {
                MainScreenViewModel(PhotosMetaDataReporterImpl(reportWriter), reportWriter, FilePathSupplier(this@MainActivity.applicationContext))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(viewModel)
        }
    }
}
