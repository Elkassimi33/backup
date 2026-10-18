
class CategoryRequest {
    int categoryId;
    String name;
    String description;
    String imageUrl;

    CategoryRequest({required this.categoryId, required this.name,
        required this.description, required this.imageUrl});


    @override
    bool operator ==(Object other) {

        if (identical(other, this))  return true;
        return other is CategoryRequest && other.categoryId ==this.categoryId;
    }

    int get hashCode => Object.hash(categoryId, name);


}
