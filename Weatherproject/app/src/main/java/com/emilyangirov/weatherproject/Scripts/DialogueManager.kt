package com.emilyangirov.weatherproject.Scripts

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object DialogueManager {
    fun locationSettingsDialogue(context: Context, listener: ReloadListener){
        val builder = AlertDialog.Builder(context)
        val dialogue = builder.create()
        dialogue.setTitle("Enable GPS?")
        dialogue.setMessage("Do you want to enable GPS?")
        dialogue.setButton(AlertDialog.BUTTON_POSITIVE, "Yes"){_,_->
            listener.onClick()
            dialogue.dismiss()
        }
        dialogue.setButton(AlertDialog.BUTTON_NEGATIVE, "No"){_,_
            ->dialogue.dismiss()
        }
        dialogue.show()
    }

    fun SearchByName(context: Context, listener: FindListener){
        val builder = AlertDialog.Builder(context)
        val nameEditor = EditText(context)
        builder.setView(nameEditor)
        val dialogue = builder.create()
        dialogue.setMessage("City name:")
        dialogue.setButton(AlertDialog.BUTTON_POSITIVE, "Yes"){_,_->
            listener.onClick(nameEditor.text.toString())
            dialogue.dismiss()
        }
        dialogue.setButton(AlertDialog.BUTTON_NEGATIVE, "No"){_,_
            ->dialogue.dismiss()
        }
        dialogue.show()
    }

    interface ReloadListener{
        fun onClick()
    }

    interface FindListener{
        fun onClick(name: String)
    }
}