package fastcampus.part1.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import fastcampus.part1.gallery.databinding.ActivityMainBinding

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

/**
 * 권한 요청 원칙
 *
 * 1. 관련 기능 사용 시 권한 요청
 * 2. 사용자 차단 금지
 * */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE
        )
        // requestCode : 작업의 고유 식별자 ('내가' 권한 요청의 고유 식별자 100으로 설정)
    }

    private fun loadImage() {

    }

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}