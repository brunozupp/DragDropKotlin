package com.novelitech.dragdropkotlin

import android.content.ClipData
import android.content.ClipDescription
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.novelitech.dragdropkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.llTop.setOnDragListener(dragListiner)
        binding.llBottom.setOnDragListener(dragListiner)

        binding.dragView.setOnLongClickListener {

            val clipText = "This is our ClipData text"

            val item = ClipData.Item(clipText)

            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)

            val data = ClipData(clipText, mimeTypes, item)

            /**
             * Created to give the View a shadow, so it gives visual feedback that is moving
             */
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE

            true
        }
    }

    val dragListiner = View.OnDragListener { view, event ->
        when(event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> { // it's called when the dragged view enters the layout boundaries
                view.invalidate() // update the layout view
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> { // it's called when the layout dragged lives the layout's boundaries
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()

                view.invalidate()

                // Remove the view from the layout it was before and update to the new layout

                val v = event.localState as View // The Drag view

                val owner = v.parent as ViewGroup // The layout where the drag view was before

                owner.removeView(v)

                // Get the layout where the view was dropped

                val destination = view as LinearLayout
                destination.addView(v)

                v.visibility = View.VISIBLE

                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false
        }
    }
}