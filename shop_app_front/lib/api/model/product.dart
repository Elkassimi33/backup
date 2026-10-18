class Product  {



    int productId;
    String categoryName;
    String name;
    double price;
    String description;
    double discount;
    String imageUrl;


     Product({required this.name, required this.description,
        required this.categoryName, required this.discount,
        required this.price, required this.imageUrl, required this.productId});

    @override
      bool operator ==(Object other) {
        if(identical(other, this)) return true;
        return other is Product && other.productId == this.productId;
      }
    @override
      int get hashCode => Object.hash(productId, categoryName);



}

