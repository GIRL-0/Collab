import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.CalendarData
import com.example.collab.R

class PersonalCalendarAdapter (val items:ArrayList<CalendarData>):RecyclerView.Adapter<PersonalCalendarAdapter.ViewHolder>(){
    interface OnItemClickListener{
        fun OnItemClick(data:CalendarData)
    }
    fun moveItem(oldPos:Int, newPos:Int){
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos,item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.planDate)
        init {
            textView.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].planDate
    }

    override fun getItemCount(): Int {
        return items.size
    }
}