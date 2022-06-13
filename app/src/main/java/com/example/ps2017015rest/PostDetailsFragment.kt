package com.example.ps2017015rest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.ps2017015rest.api.PostApi
import com.example.ps2017015rest.databinding.FragmentPostDetailsBinding
import com.example.ps2017015rest.model.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailsFragment : Fragment() {
    companion object {
        private val TAG = PostDetailsFragment::class.java.simpleName
        const val POST_ID = "postId"
    }

    private var _binding: FragmentPostDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val postId = arguments?.getInt(POST_ID)

        postId?.let { PostApi.retrofitService.getPost(it) }?.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val post = response.body()
                if (post != null) {
                    binding.textviewPostId.text = post.id.toString()
                    binding.textviewPostTitle.text = post.title
                    binding.textviewPostBody.text = post.body

                    binding.buttonComments.setOnClickListener {
                        val action = PostDetailsFragmentDirections
                            .actionPostDetailsFragmentToCommentsListFragment(
                                postId = post.id
                            )

                        it.findNavController().navigate(action)
                    }
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.e(TAG, "Got error : " + t.localizedMessage)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}