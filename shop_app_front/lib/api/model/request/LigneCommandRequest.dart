
class LigneCommandRequest {

        int ligneCommandId;

        int productId;

        int quantity;

        double priceUnitaire;

        double sousTotal;

    LigneCommandRequest({required this.ligneCommandId, required this.productId,
        required this.quantity, required this.sousTotal, required this.priceUnitaire});

    @override
      bool operator ==(Object other) {
        if(identical(other, this)) return true;

        return other is LigneCommandRequest && this.ligneCommandId == other.ligneCommandId;

      }

    @override
      int get hashCode => Object.hash(ligneCommandId,productId);


}
