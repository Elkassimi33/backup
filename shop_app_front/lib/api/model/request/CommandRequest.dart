import 'package:shop_app_front/api/model/request/LigneCommandRequest.dart';

class CommandRequest {

        int commandId;

        int userId;

        DateTime dateCommand;

        DateTime? updatedAt;

        CommandStatus status;

        double totalPrice;

        List<LigneCommandRequest> ligneCommands;

    CommandRequest({required this.commandId,required this.userId,
        required this.dateCommand, this.updatedAt, required this.status,
        required this.totalPrice, required this.ligneCommands
    });

    @override
    bool operator == (Object other){
        if(identical(other, this)) return true;
        return other is CommandRequest && this.commandId == this.commandId;
    }

    @override
    int get hashCode => Object.hash(commandId, userId);


}

enum CommandStatus {
  ACCEPTED,
  LIVREE,
  ANULER
}
