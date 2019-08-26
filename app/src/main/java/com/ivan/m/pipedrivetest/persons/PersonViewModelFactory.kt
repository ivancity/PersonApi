package com.ivan.m.pipedrivetest.persons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.m.pipedrivetest.repo.PersonRepository

@Suppress("UNCHECKED_CAST")
class PersonViewModelFactory(private val personRepository: PersonRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(PersonViewModel::class.java) ->
                    PersonViewModel(personRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T
    }
}