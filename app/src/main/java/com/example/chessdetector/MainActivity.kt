package com.example.chessdetector

import android.content.ClipData
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainActivity : AppCompatActivity() {

    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        fab.setOnClickListener { view ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // start the image capture Intent
            startActivityForResult(intent, 1337)
        }

        val cells = mutableListOf(
            R.id.a8, R.id.b8, R.id.c8, R.id.d8, R.id.e8, R.id.f8, R.id.g8, R.id.h8,
            R.id.a7, R.id.b7, R.id.c7, R.id.d7, R.id.e7, R.id.f7, R.id.g7, R.id.h7,
            R.id.a6, R.id.b6, R.id.c6, R.id.d6, R.id.e6, R.id.f6, R.id.g6, R.id.h6,
            R.id.a5, R.id.b5, R.id.c5, R.id.d5, R.id.e5, R.id.f5, R.id.g5, R.id.h5,
            R.id.a4, R.id.b4, R.id.c4, R.id.d4, R.id.e4, R.id.f4, R.id.g4, R.id.h4,
            R.id.a3, R.id.b3, R.id.c3, R.id.d3, R.id.e3, R.id.f3, R.id.g3, R.id.h3,
            R.id.a2, R.id.b2, R.id.c2, R.id.d2, R.id.e2, R.id.f2, R.id.g2, R.id.h2,
            R.id.a1, R.id.b1, R.id.c1, R.id.d1, R.id.e1, R.id.f1, R.id.g1, R.id.h1
        )
        val black_pawn = R.drawable.black_pawn
        val black_king = R.drawable.black_king
        val black_queen = R.drawable.black_queen
        val black_knight = R.drawable.black_knight
        val black_bishop = R.drawable.black_bishop
        val black_rook = R.drawable.black_rook

        val white_pawn = R.drawable.white_pawn
        val white_king = R.drawable.white_king
        val white_queen = R.drawable.white_queen
        val white_knight = R.drawable.white_knight
        val white_bishop = R.drawable.white_bishop
        val white_rook = R.drawable.white_rook

        var chess_positions = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"
        chess_positions = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR"
        chess_positions = "rnb1kbnr/pp1qpp1p/B1p3p1/3P4/5Q2/3P3N/PPPB1PPP/RN2K2R"
        val splited = chess_positions.split("/")

        val eights = cells.slice(0..7)
        val seventh = cells.slice(8..15)
        val sixth = cells.slice(16..23)
        val fifth = cells.slice(24..31)
        val fourth = cells.slice(32..39)
        val threes = cells.slice(40..47)
        val twos = cells.slice(48..55)
        val firsts = cells.slice(56..63)

        val next_cells = mutableListOf(eights, seventh, sixth, fifth, fourth, threes, twos, firsts)

        fun set_cells(cell: Int, value: Char) {
            when (value) {
                'p' -> findViewById<ImageView>(cell).setImageResource(black_pawn)
                'r' -> findViewById<ImageView>(cell).setImageResource(black_rook)
                'n' -> findViewById<ImageView>(cell).setImageResource(black_knight)
                'b' -> findViewById<ImageView>(cell).setImageResource(black_bishop)
                'q' -> findViewById<ImageView>(cell).setImageResource(black_queen)
                'k' -> findViewById<ImageView>(cell).setImageResource(black_king)

                'P' -> findViewById<ImageView>(cell).setImageResource(white_pawn)
                'R' -> findViewById<ImageView>(cell).setImageResource(white_rook)
                'N' -> findViewById<ImageView>(cell).setImageResource(white_knight)
                'B' -> findViewById<ImageView>(cell).setImageResource(white_bishop)
                'Q' -> findViewById<ImageView>(cell).setImageResource(white_queen)
                'K' -> findViewById<ImageView>(cell).setImageResource(white_king)
            }
        }

        for ((ind, cello) in next_cells.withIndex()) {
            var counter = 0
            var offset = 0
            for ((index, cell) in cello.withIndex()) {
                if (counter != 0) {
                    findViewById<ImageView>(cell).setImageResource(0)
                    counter -= 1
                    offset += 1
                    continue
                }
                val value = splited[ind][index - offset]
                when (value.isDigit()) {
                    false -> set_cells(cell, value)
                    true -> {
                        counter = value.toString().toInt() - 1
                        findViewById<ImageView>(cell).setImageResource(0)
                    }
                }
            }
        }


        // This defines your touch listener
        class MyTouchListener : View.OnTouchListener {
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val view = view as ImageView
                    val data = ClipData.newPlainText("", "")
                    val shadowBuilder = View.DragShadowBuilder(
                        view
                    )
                    view.startDrag(data, shadowBuilder, view, 0)
//                    view.setImageResource(0)
                    return true
                } else {
                    return false
                }
            }
        }

        class MyDragListener : View.OnDragListener {
            override fun onDrag(v: View, event: DragEvent): Boolean {
                val container = v as ImageView
                val view = event.localState as ImageView
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                    }
//                    DragEvent.ACTION_DRAG_ENTERED -> container.setImageResource(0)
                    DragEvent.ACTION_DRAG_EXITED -> {
                    }
                    DragEvent.ACTION_DROP -> {
                        // Dropped, reassign View to ViewGroup
//                        val owner = view.parent as ViewGroup
                        Log.d("TAG", "${view.drawable}")
                        container.setImageDrawable(view.drawable)

                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        view.setImageResource(0)}
                    else -> {
                    }
                }// do nothing
                return true
            }
        }

        for (cell in cells){
            findViewById<ImageView>(cell).setOnTouchListener(MyTouchListener())
            findViewById<ImageView>(cell).setOnDragListener(MyDragListener())
        }
//        val qwerty = findViewById(R.id.c2) as ImageView
//        val qwerty1 = findViewById(R.id.c3) as ImageView
//
//        qwerty.setOnTouchListener(MyTouchListener())
//        qwerty1.setOnDragListener(MyDragListener())
//
//        // set on-click listener
//        qwerty.setOnClickListener {
//            // your code to perform when the user clicks on the ImageView
//            qwerty.setImageResource(white_king)
//        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (id) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
