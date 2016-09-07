$('.thumbnail-image').on('click',function()
    {
        var imageId = $(this).attr('data-main-image-id'); 
        $('#large-image').attr('src','/kuvat/'+imageId);
        var x = $('#large-image-modal');
        $('#large-image-modal').modal('show');
    });

