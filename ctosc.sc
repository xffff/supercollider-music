{\rtf1\ansi\ansicpg1252\cocoartf1038\cocoasubrtf360
{\fonttbl\f0\fnil\fcharset0 Monaco;}
{\colortbl;\red255\green255\blue255;\red0\green0\blue191;\red0\green0\blue0;\red0\green115\blue0;
\red0\green0\blue255;\red191\green0\blue0;\red255\green102\blue0;\red51\green51\blue191;\red102\green102\blue191;
\red102\green102\blue191;\red0\green115\blue0;}
\deftab720
\pard\pardeftab720\ql\qnatural

\f0\fs18 \cf2 Event\cf3 .addEventType(\cf4 \\ctosc\cf3 , \{\cf5 |server|\cf3 \
\pard\tx560\tx1120\tx1680\tx2240\tx2800\tx3360\tx3920\tx4480\tx5040\tx5600\tx6160\tx6720\ql\qnatural\pardirnatural
\cf3 	\cf5 var\cf3  freqs, lag, dur, sustain, strum;\
	\cf5 var\cf3  bndl, oscout, hasGate, osccmd;	\
	\cf5 var\cf3  oscEventFunctions = (\
			\cf4 \\noteon\cf3 :			#\{ \cf5 arg\cf3  voicename, midinote=60, amp=0.1;\
								[voicename, midinote, asInteger((amp * 127).clip(0, 127))] \},\
			\cf4 \\noteoff\cf3 : 		#\{ \cf5 arg\cf3  voicename, midinote=60;\
								[voicename, midinote] \},\
			\cf4 \\detuned_noteon\cf3 :  	#\{ \cf5 arg\cf3  voicename, midinote=60, detune=0, amp=0.1;\
								[voicename, midinote, detune, asInteger((amp * 127).clip(0, 127))] \},\
			\cf4 \\detuned_noteoff\cf3 :  #\{ \cf5 arg\cf3  voicename, midinote=60, detune=0;\
								[voicename, midinote, detune] \},\
			\cf4 \\ctnote\cf3 :			#\{ \cf5 arg\cf3  voicenumber=0, noteid=0, midinote=60, amp=0.1, dur=0;\
								[voicenumber, noteid, midinote, asInteger((amp * 127).clip(0, 127)), dur] \},\
			\cf4 \\ctnoteoff\cf3 :  		#\{ \cf5 arg\cf3  noteid=0;\
								[noteid] \},\
			\cf4 \\program\cf3 :  		#\{ \cf5 arg\cf3  voicename, progname=1; \
								[ voicename, progname ] \},\
			\cf4 \\gain\cf3 : 			#\{ \cf5 arg\cf3  voicename, gain=0; \cf6 // this is in dB \cf3 \
								[ voicename, gain ] \},\
			\cf4 \\allNotesOff\cf3 : 	#\{ \cf5 arg\cf3  null=0; [null] \},\
			\cf4 \\kammerton\cf3 : 		#\{\cf5 arg\cf3  freq=442; [freq]\}\
	);\
		\
	freqs = \cf7 ~freq\cf3  = \cf7 ~detunedFreq\cf3 .value;\
\
	if (freqs.isRest.not) \{\
		\cf7 ~amp\cf3  = \cf7 ~amp\cf3 .value;\
		\cf7 ~midinote\cf3  = freqs.cpsmidi;\
		strum = \cf7 ~strum\cf3 ;\
		lag = \cf7 ~lag\cf3 ;\
		sustain = \cf7 ~sustain\cf3  = \cf7 ~sustain\cf3 .value;\
		oscout = \cf7 ~oscout\cf3 .value; \cf6 // OSC address\cf3 \
		hasGate = \cf7 ~hasGate\cf3  ? \cf8 true\cf3 ;\
		osccmd = \cf7 ~osccmd\cf3 ;\
		bndl = oscEventFunctions[osccmd].valueEnvir.asCollection;\
\
		bndl = bndl.asControlInput.flop;\
				\
		bndl.do \{\cf5 |msgArgs, i|\cf3 \
				\cf5 var\cf3  latency;\
\
				latency = i * strum + lag;\
\
				if(latency == 0.0) \{\
					oscout.sendBundle(latency, [osccmd, msgArgs].flat)\
				\} \{\
					\cf9 thisThread\cf3 .clock.sched(latency, \{\
						oscout.sendBundle(latency, [osccmd, msgArgs].flat)\
					\})\
				\};\
				\
				\cf6 // automatically deal with noteoff messages for each note-type\cf3 \
				case \
					\{hasGate and: \{ osccmd === \cf4 \\noteon\cf3  \}\} \cf0 \{\
					\cf10 thisThread\cf0 .clock.sched(sustain + latency, \{\
						oscout.sendBundle(latency, [\cf11 \\noteoff\cf0 , msgArgs].flat)\
					\})\}\
					\{hasGate and: \{ osccmd === \cf11 \\detuned_noteon\cf0  \}\} \{\
					\cf10 thisThread\cf0 .clock.sched(sustain + latency, \{\
						oscout.sendBundle(latency, [\cf11 \\detuned_noteoff\cf0 , msgArgs].flat)\
					\})\}\
					\{hasGate and: \{ osccmd === \cf11 \\ctnote\cf0  \}\} \{\
					\cf10 thisThread\cf0 .clock.sched(sustain + latency, \{\
						oscout.sendBundle(latency, [\cf11 \\ctnoteoff\cf0 , msgArgs].flat)\
					\})\};\cf3 									\
		\};\
	\}\
\});\
\
}