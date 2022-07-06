import SwiftUI
import shared
import Combine



private let log = koin.loggerWithTag(tag: "ViewController")


class ObservableScheduleModel : ObservableObject {
    private var viewModel : ScheduleCallbackViewModel?
    
    @Published
    var loading = false
    
    func activate(){
        let viewModel = KotlinDependencies.shared.getScheduleViewModel()
        self.viewModel = viewModel
    }
    
    func refresh() {
        viewModel?.refreshSchedule()
    }
    
}





struct ContentView: View {
	let greet = Greeting().greeting()
    @ObservedObject
    var observableModel = ObservableScheduleModel()
	var body: some View {
		Text(greet)
            .onAppear(perform: {
                observableModel.activate()
                observableModel.refresh()
            })
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
