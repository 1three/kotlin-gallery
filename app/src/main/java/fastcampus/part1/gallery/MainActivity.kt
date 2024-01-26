package fastcampus.part1.gallery

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
            checkReadImagesPermission()
        }
    }

    private fun checkReadImagesPermission() {
        // 권한 유무 확인 및 요청
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> loadImage()

            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_MEDIA_IMAGES
            ) -> {
                showReadImagesPermissionRationale()
            }

            else -> {
                requestReadImagesPermission()
            }
        }
    }

    private fun showReadImagesPermissionRationale() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해\n갤러리 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("확인") { _, _ ->
                requestReadImagesPermission()
            }
        }.show()
    }

    private fun requestReadImagesPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_READ_MEDIA_IMAGES
        )
        // requestCode : 작업의 고유 식별자 ('내가' 권한 요청의 고유 식별자 100으로 설정)
    }

    private fun loadImage() {
        Toast.makeText(this, "갤러리로 이동합니다.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val REQUEST_READ_MEDIA_IMAGES = 100
    }
}