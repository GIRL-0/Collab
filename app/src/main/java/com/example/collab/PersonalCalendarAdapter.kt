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
    var itemClickListener:OnItemClickListener?=null

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val planDate = itemView.findViewById<TextView>(R.id.planDate)
        val planTime = itemView.findViewById<TextView>(R.id.planTime)
        val planContent = itemView.findViewById<TextView>(R.id.planContent)
        val calendarLinearLayout = itemView.findViewById<TextView>(R.id.calendarLinearLayout)
        init {
            planDate.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }
            calendarLinearLayout.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_row, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.planDate.text = items[position].planDate
        holder.planTime.text = items[position].planTime
        holder.planContent.text = items[position].planContent
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //    fun moveItem(oldPos:Int, newPos:Int){
//        val item = items[oldPos]
//        items.removeAt(oldPos)
//        items.add(newPos,item)
//        notifyItemMoved(oldPos, newPos)
//    }
//
//    fun removeItem(pos:Int){
//        items.removeAt(pos)
//        notifyItemRemoved(pos)
//    }

}
