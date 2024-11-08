    // Function to confirm deletion and send delete request
    async function confirmDelete(id) {
        // Confirm before proceeding with delete
        if (confirm("이 음원을 삭제하시겠습니까?")) {
            try {
                // Send DELETE request to the server
                const response = await fetch(`/admin/sounds/${id}`, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' }
                });

                if (response.ok) {
                    alert("음원이 삭제되었습니다.");
                    // Optionally, reload the page or remove the row from the table
                    location.reload();
                } else {
                    alert("삭제 실패. 다시 시도해 주세요.");
                }
            } catch (error) {
                console.error("삭제 중 오류가 발생했습니다:", error);
                alert("삭제 중 오류가 발생했습니다.");
            }
        }
    }
