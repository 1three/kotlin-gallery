package fastcampus.part1.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * 나만의 갤러리 앱
 *
 * 1. 권한 처리
 * 2. 갤러리 내 이미지 가져오기
 * 3. 여러 가지 타입의 리스트 구현
 * */

/**
 * RecyclerView - ListAdapter
 * ViewPager2
 * Toolbar
 * sealed class
 * data class
 * Permission
 * Storage Access Framework
 * registerForActivityResult
 * */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}