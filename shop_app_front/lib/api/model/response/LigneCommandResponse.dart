class Lignecommandresponse {
    int productId;
    int quantity;

    Lignecommandresponse({required this.productId,required this.quantity});


    bool operator == (Object other){
        if(identical(other, this)) return true;
        return other is Lignecommandresponse && other.productId == this.productId;
    }

    int get hashCode => Object.hash(productId, quantity);

}
