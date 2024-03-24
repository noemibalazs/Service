package com.noemi.service

import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noemi.service.util.toFile
import com.noemi.service.util.toMultipartBody
import com.noemi.service.workmanager.datasource.ImgurRemoteDataSource
import com.noemi.service.workmanager.datasource.ImgurRemoteDataSourceImpl
import com.noemi.service.workmanager.model.ImgurResponse
import com.noemi.service.workmanager.network.ImgurAPI
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import okhttp3.MultipartBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import java.io.File

@RunWith(AndroidJUnit4::class)
class ImgurRemoteDataSourceImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var imgurAPI: ImgurAPI

    @MockK
    private lateinit var remoteDataSource: ImgurRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = ImgurRemoteDataSourceImpl(
            imgurAPI = imgurAPI,
            context = ApplicationProvider.getApplicationContext()
        )
    }

    @Test
    fun `test upload image and successful`() {
        val response = mockk<Call<ImgurResponse>>()
        val uri = Uri.parse("test_image_path")
        val file = mockk<File>()
        val multiPart = mockk<MultipartBody.Part>()

        mockkStatic(Uri::toFile)
        every { any<Uri>().toFile() } returns file

        mockkStatic(File::toMultipartBody)
        every { any<File>().toMultipartBody() } returns multiPart

        every { imgurAPI.postImage(multiPart) } returns response

        remoteDataSource.uploadImage(uri)

        verify(exactly = 1) { imgurAPI.postImage(multiPart) }
    }

}