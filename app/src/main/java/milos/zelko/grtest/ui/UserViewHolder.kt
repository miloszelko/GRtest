package milos.zelko.grtest.ui

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item.view.*

class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun setName(name:String) {
        itemView.tvName.text = name
    }

    fun setEmail(email: String) {
        itemView.tvEmail.text = email
    }

    fun setAvatar(url: String) {
        Picasso.get().load(url).into(itemView.ivAvatar as ImageView)
    }
}