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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val planContent = itemView.findViewById<TextView>(R.id.planContent)
        val planStartDate = itemView.findViewById<TextView>(R.id.planStartDate)
        val planEndDate = itemView.findViewById<TextView>(R.id.planEndDate)
        val planStartTime = itemView.findViewById<TextView>(R.id.planStartTime)
        val planEndTime = itemView.findViewById<TextView>(R.id.planEndTime)
        init {
            planContent.setOnClickListener{
                itemClickListener?.OnItemClick(items[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.planContent.text = items[position].planContent
        holder.planStartDate.text = items[position].planStartDate
        holder.planEndDate.text = items[position].planEndDate
        holder.planStartTime.text = items[position].planStartTime
        holder.planEndTime.text = items[position].planEndTime
    }

    override fun getItemCount(): Int {
        return items.size
    }
}