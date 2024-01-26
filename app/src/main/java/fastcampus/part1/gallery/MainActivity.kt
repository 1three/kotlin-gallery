package fastcampus.part1.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
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
 * Storage Access Framework (SAF, 단일 앱뿐만 아니라 모든 제공자에 불러오기 가능)
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
    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList)
        }
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.apply {
            title = "사진 가져오기"
            setSupportActionBar(this)
        }

        initRecyclerView()

        binding.loadImageButton.setOnClickListener {
            checkReadImagesPermission()
        }

        binding.navigateFrameActivityButton.setOnClickListener {
            navigateToFrameActivity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                checkReadImagesPermission()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(object : ImageAdapter.ItemClickListener {
            override fun onLoadMoreClick() {
                checkReadImagesPermission()
            }
        })

        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2) // 2*n
        }
    }

    private fun checkReadImagesPermission() {
        // 권한 유무 확인 및 요청
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> loadImages()

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

    private fun requestReadImagesPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_READ_MEDIA_IMAGES
        )
        // requestCode : 작업의 고유 식별자 ('내가' 권한 요청의 고유 식별자 100으로 설정)
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

    private fun loadImages() {
        Toast.makeText(this, "갤러리로 이동합니다.", Toast.LENGTH_SHORT).show()
        imageLoadLauncher.launch("image/*")
    }

    private fun updateImages(uriList: List<Uri>) {
        val images = uriList.map { ImageItems.Image(it) }
        val updatedImages = imageAdapter.currentList.toMutableList().apply {
            addAll(images)
        }
        imageAdapter.submitList(updatedImages)
        // 이전에 가져온 이미지라도, 다시 이미지 가져오기 가능
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_MEDIA_IMAGES -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImages()
                }
            }
        }
    }

    private fun navigateToFrameActivity() {
        val images =
            imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map { it.uri.toString() }
                .toTypedArray()
        val intent = Intent(this, FrameActivity::class.java).putExtra("images", images)
        startActivity(intent)
    }

    companion object {
        const val REQUEST_READ_MEDIA_IMAGES = 100
    }
}