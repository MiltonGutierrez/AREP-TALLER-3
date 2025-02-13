
const notes = (() => {

    const api = apiClient;

    const getNotes = async () => {
        try {
            let notes = await api.getNotes();
            notesContainer.innerHTML = '';

            notes.forEach(note => {
                const noteElement = document.createElement('div');
                noteElement.className = 'note';
    
                const title = document.createElement('h2');
                title.textContent = note.title;
    
                const group = document.createElement('p');
                group.textContent = `Grupo: ${note.group}`;
    
                const date = document.createElement('p');
                date.textContent = `Fecha: ${note.date}`;
    
                const content = document.createElement('p');
                content.textContent = note.content;
    
                noteElement.appendChild(title);
                noteElement.appendChild(group);
                noteElement.appendChild(date);
                noteElement.appendChild(content);
    
                notesContainer.appendChild(noteElement);
            });
        } catch (error) {
            console.error(error);
        }
    };

    const addNote = async (title, group, content) => {
        try {
            if(!title || !group || !content) {
                alert('All fields are required');
                throw new Error('All fields are required');
            }
            await api.addNote(title, group, content);
            alert('Note added');
        } catch (error) {
            console.error(error);
        }
     };

    const greeting = async (name) => {
        try{
            let greeting = await api.greeting(name);
            document.getElementById('greetingMessage').textContent = greeting.greeting;
        }
        catch (error) {
            console.error(error);
        }
    };

    return {
        getNotes,
        addNote,
        greeting
    }
})();
