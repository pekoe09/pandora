$(document).ready(function(){
    
    var deleteForm;
    
    $('.confirm-delete-modal').on('click', function(e){
        deleteForm = e.target.closest('form');
        $('#delete-modal').modal('show');        
        e.preventDefault();
    });
    $('.modal-cancel').on('click', function(){
        $(this).closest('.modal').modal('hide');
    });
    $('.modal-delete').on('click', function(){
        $(this).closest('.modal').modal('hide');       
        deleteForm.submit();
    });
});
