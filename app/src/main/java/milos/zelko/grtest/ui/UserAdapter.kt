package milos.zelko.grtest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import milos.zelko.grtest.R
import milos.zelko.grtest.model.User

class UserAdapter(private val listener: UserClickListener): PagedListAdapter<User, UserViewHolder>(
    diffCallback
) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user?.let {
            holder.setName("${it.firstName} ${it.lastName}")
            holder.setEmail(user.email)
            holder.setAvatar(user.avatar)
            holder.itemView.setOnClickListener { listener.userClicked(user) }
        }
    }

    interface UserClickListener {
        fun userClicked(user: User)
    }
}