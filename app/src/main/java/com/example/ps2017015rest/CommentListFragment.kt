package com.example.ps2017015rest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ps2017015rest.PostDetailsFragment.Companion.POST_ID
import com.example.ps2017015rest.adapter.CommentAdapter
import com.example.ps2017015rest.api.PostApi
import com.example.ps2017015rest.databinding.FragmentCommentListBinding
import com.example.ps2017015rest.model.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsListFragment : Fragment() {
    companion object {
        private val TAG = CommentsListFragment::class.java.simpleName
    }
    private var _binding:FragmentCommentListBinding?=null

    private val binding get() = _binding!!
    private lateinit var adapter: CommentAdapter
    private val comments: MutableList<Comment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerViewPostComments = binding.recyclerviewPostComments

        adapter = CommentAdapter(comments)
        recyclerViewPostComments.adapter = adapter

        fetchComments()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun fetchComments() {
        val postId = arguments?.getInt(POST_ID)
        postId?.let { PostApi.retrofitService.getComments(it) }
            ?.enqueue(object : Callback<List<Comment>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<List<Comment>>,
                    response: Response<List<Comment>>
                ) {
                    val commentsFromResponse = response.body()
                    if (commentsFromResponse != null) {
                        comments.addAll(commentsFromResponse)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                    Log.e(TAG, "Got error : " + t.localizedMessage)
                }

            })
    }
}