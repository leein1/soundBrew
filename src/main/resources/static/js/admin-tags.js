    // Enable editing mode for the tag row
    function enableTagEditing(button) {
        const row = button.closest('tr');
        row.querySelector('.current-value').style.display = 'none';
        row.querySelector('.editable-field').style.display = 'inline';

        button.style.display = 'none';
        row.querySelector('.apply-button').style.display = 'inline';
        row.querySelector('.cancel-button').style.display = 'inline';
    }

    // Apply the tag name change to the backend
    async function applyTagChanges(button, tagType, oldTagName) {
        const row = button.closest('tr');
        const newTagName = row.querySelector('.editable-field').value;

        if (newTagName.trim() === "") {
            alert("Please enter a new tag name.");
            return;
        }

        // Send patch request to update the tag
        await fetch(`/admin/tags/`+tagType, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tagType, oldTagName, newTagName })
        });

        alert("Tag updated successfully.");
        location.reload();
    }

    // Cancel the editing mode and restore the original view
    function cancelTagChanges(button) {
        const row = button.closest('tr');
        row.querySelector('.editable-field').style.display = 'none';
        row.querySelector('.current-value').style.display = 'inline';

        row.querySelector('.edit-button').style.display = 'inline';
        row.querySelector('.apply-button').style.display = 'none';
        row.querySelector('.cancel-button').style.display = 'none';
    }

    // Create a new tag in the backend
    async function createNewTag() {
        if(confirm("태그의 타입, 네임을 확인하고 정말로 태그를 생성하시겠습니까?")){
            try{
                const tagType = document.getElementById('newTagType').value;
                const tagName = document.getElementById('newTagName').value;

                if (tagName.trim() === "") {
                    alert("Please enter a tag name.");
                    return;
                }

                // Send request to create the new tag
                await fetch(`/admin/tags/`+tagType, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify( tagName )
                });

                alert("New tag created successfully.");
                location.reload();
            }
         catch (error) {
                console.error("생성 중 오류가 발생했습니다:", error);
                alert("생성 중 오류가 발생했습니다.");
            }
        }
    }
